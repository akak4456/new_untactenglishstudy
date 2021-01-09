package com.example.study.engcontent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.engcontent.adapter.VocaPagerAdapter;
import com.example.study.engcontent.vo.EnglishDictionary;
import com.example.study.engcontent.vo.EnglishDictionaryPageMaker;
import com.example.study.engcontent.vo.VocaItem;
import com.example.study.engcontent.vo.VocabularyPageResponse;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VocaCardActivity extends AppCompatActivity {
    private final int PAGE_SIZE = 20;

    private ViewPager viewPager;
    private VocaPagerAdapter pagerAdapter;
    ArrayList<String> word;
    ArrayList<String> mean;
    private Context context;
    private Long gno;
    private Long vno;
    private int totalPage;
    private int pageNum = 2;
    private boolean loading;
    private int selectedIndex;
    private boolean mPageEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_card);
        context =this;
        gno = getIntent().getLongExtra("gno",0L);
        vno = getIntent().getLongExtra("vno",0L);
        word = new ArrayList<>();
        mean = new ArrayList<>();
        // 현재는 임의로 추가
        // 서버 연동 필요
        viewPager = (ViewPager) findViewById(R.id.viewPager_voca);
        pageNum = 2;
        loading = false;
        mPageEnd = false;
        getPage(1,PAGE_SIZE);
        //끝에 도달하면 새로운 페이지를 불러라
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedIndex = position;
                if(mPageEnd){
                    mPageEnd = false;
                    if(!loading){
                        loading = true;
                        if(pageNum <= totalPage) {
                            getPage(pageNum, PAGE_SIZE);
                            pageNum++;
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(!loading&&selectedIndex == pagerAdapter.getCount() - 1){
                    mPageEnd = true;
                }
            }
        });

        Button btn_see_table = findViewById(R.id.btn_see_table);
        btn_see_table.setOnClickListener(view->{
            Intent intent = new Intent(context, VocaTableActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",vno);
            context.startActivity(intent);
            finish();
        });
    }
    private void getPage(int page, int size){
        new JsonRequestUtil<EnglishDictionaryPageMaker>(context).request(
                Request.Method.GET,
                "/vocabulary/"+gno+"/"+vno+"?page="+page+"&size="+size,
                EnglishDictionaryPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<EnglishDictionary>>(){}.getType();
                    List<EnglishDictionary> list = new Gson().fromJson(json,listType);
                    //부른 데이터를 이용해서 recycler view를 바꾼 뒤에 recycler view가 맨 위로 갈 것인데 저장한 스크롤 위치로 되돌아간다
                    for(EnglishDictionary dir:list){
                        word.add(dir.getEnglishSpelling().getSpelling());
                        mean.add(dir.getPartOfSpeech()+"\n"+dir.getMeaning());
                    }
                    pagerAdapter = new VocaPagerAdapter(this,word,mean);
                    viewPager.setAdapter(pagerAdapter);
                    if(page >= 2)
                        viewPager.setCurrentItem(selectedIndex+1);
                    loading = false;
                },
                error -> {

                }
        );
    }
}