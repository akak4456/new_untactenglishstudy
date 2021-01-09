package com.example.study.engcontent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.engcontent.adapter.QuizListAdapter;
import com.example.study.engcontent.adapter.VocaListAdapter;
import com.example.study.engcontent.vo.VocaItem;
import com.example.study.engcontent.vo.VocabularyPageResponse;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class QuizSelectVocaActivity extends AppCompatActivity {
    private Context context;
    private boolean loading;
    private RecyclerView recyclerView;
    private QuizListAdapter adapter;
    private int totalPage;//총페이지수
    private int pageNum;//현재 페이지수 + 1
    private Parcelable recyclerViewState;//스크롤 위치 저장
    private Long gno;
    private int quiz_case;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select_voca);
        context = this;
        gno = getIntent().getLongExtra("gno", 0L);
        quiz_case = getIntent().getIntExtra("quiz_case",0);
    }



    @Override
    public void onStart(){
        super.onStart();
        initRecyclerView();
        if(adapter != null)
            adapter.clearItems();
        adapter = new QuizListAdapter(gno,quiz_case);
        pageNum = 2;
        loading = false;
        getPage(1,10);//일단 처음 페이지를 불러온다
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.voca_listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loading&&!recyclerView.canScrollVertically(1)){
                    //스크롤의 마지막에 닿으면
                    loading = true;
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//일단 현재 스크롤된 곳을 저장한 뒤에
                    if(pageNum <= totalPage)
                        getPage(pageNum,10);//데이터를 불러와서 view를 바꾼다.
                    pageNum++;//다음 페이지를 부를 준비를 한다
                }
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void getPage(int page, int size){
        new JsonRequestUtil<VocabularyPageResponse>(context).request(
                Request.Method.GET,
                "/vocabulary/"+gno+"?page="+page+"&size="+size,
                VocabularyPageResponse.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getPage().getTotalPages();
                    String json = new Gson().toJson(response.getPage().getResult().getContent());
                    Type listType = new TypeToken<List<VocaItem>>(){}.getType();
                    List<VocaItem> list = new Gson().fromJson(json,listType);
                    //부른 데이터를 이용해서 recycler view를 바꾼 뒤에 recycler view가 맨 위로 갈 것인데 저장한 스크롤 위치로 되돌아간다
                    if(page == 1) {
                        list.add(0, new VocaItem(0L, "기본단어장", response.getDefaultVocaCount()));
                    }
                    adapter.addItems(list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    loading = false;
                },
                error -> {

                }
        );
    }
}
