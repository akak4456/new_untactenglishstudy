package com.example.study.engcontent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.db.MyTable;
import com.example.study.db.MyTableList;
import com.example.study.engcontent.adapter.VocaPagerAdapter;
import com.example.study.engcontent.vo.EnglishDictionary;
import com.example.study.engcontent.vo.EnglishDictionaryPageMaker;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VocaTableActivity extends AppCompatActivity {
    private final int PAGE_SIZE = 20;
    private TableLayout tableLayout;
    private Context context;
    private Long gno;
    private Long vno;
    private TextView tv_table_num;
    private int totalPage;
    private boolean loading = false;
    private int pageNum = 1;
    private Button btn_left;
    private Button btn_right;
    private ArrayList<String> word;
    private ArrayList<String> mean;
    private boolean lastCalled = false;
    private Long totalElements;
    private MyTable myTable;
    private Set<Integer> checkedIds;//check가 된 checkbox id 리스트들
    private ArrayList<MyTableList> myTableList;
    private int checkedCount = 0;//페이지마다 체크된 체크박스의 개수
    private int maxCheckedCount = 0;//페이지마다 있는 최대 체크박수 갯수
    private CheckBox checkbox_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_table);

        tableLayout = (TableLayout) findViewById(R.id.voca_table);
        context= this;

        gno = getIntent().getLongExtra("gno",0L);
        vno = getIntent().getLongExtra("vno",0L);
        word = new ArrayList<>();
        mean = new ArrayList<>();
        myTable = new MyTable(context);
        myTableList = myTable.select(vno.intValue());
        checkedIds = new HashSet<>();
        for(int i=0;i<myTableList.size();i++){
            Log.i("MY TABLE",myTableList.get(i).vocaid+":"+myTableList.get(i).itemid);
            checkedIds.add(myTableList.get(i).itemid);
        }
        Button btn_see_card = findViewById(R.id.btn_see_card);
        btn_see_card.setOnClickListener(view->{
            Intent intent = new Intent(context, VocaCardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",vno);
            context.startActivity(intent);
            finish();
        });
        getPage(1,PAGE_SIZE);
        btn_left = findViewById(R.id.btn_left);
        btn_left.setOnClickListener(view->{
            addRows(PAGE_SIZE*(pageNum-2),Math.min(PAGE_SIZE*(pageNum-1),word.size()));
            pageNum--;
            tv_table_num.setText((pageNum*PAGE_SIZE)+"/"+totalElements);
            if(pageNum == 1){
                btn_left.setVisibility(View.INVISIBLE);
            }
            btn_right.setVisibility(View.VISIBLE);
        });
        btn_left.setVisibility(View.INVISIBLE);
        btn_right = findViewById(R.id.btn_right);
        btn_right.setOnClickListener(view->{
            pageNum++;
            tv_table_num.setText(Math.min((pageNum*PAGE_SIZE),totalElements)+"/"+totalElements);
            if(!lastCalled&&word.size() < PAGE_SIZE*pageNum) {
                getPage(pageNum, PAGE_SIZE);//데이터를 불러와야만 한다.
            }else {
                addRows(PAGE_SIZE * (pageNum - 1),Math.min(PAGE_SIZE * pageNum, word.size()));//데이터를 불러오지 않아도 된다.
            }
            if(pageNum == totalPage){
                lastCalled = true;
                btn_right.setVisibility(View.INVISIBLE);
            }
            btn_left.setVisibility(View.VISIBLE);
        });

        tv_table_num = findViewById(R.id.tv_table_num);

        checkbox_all = findViewById(R.id.checkbox_all);
        checkbox_all.setOnClickListener(view->{
            //페이지에 있는 체크박스 모두 체크하거나 체크해제 한다.
            for(int index = 0; index < ((ViewGroup) tableLayout).getChildCount(); index++) {
                View row = ((ViewGroup) tableLayout).getChildAt(index);
                if(row instanceof TableRow){
                    for(int iter = 0;iter<((ViewGroup) row).getChildCount();iter++){
                        View nextChild = ((ViewGroup)row).getChildAt(iter);
                        if(nextChild instanceof CheckBox){
                            CheckBox chk = (CheckBox) nextChild;
                            chk.setChecked(checkbox_all.isChecked());
                        }
                    }
                }
            }
        });
    }
    private void addRows(int start,int end){
        tableLayout.removeViews(1, Math.max(0, tableLayout.getChildCount() - 1));//헤더를 제외한 모든 행을 비운 뒤에 받아온 데이터로 행을 추가한다.
        checkedCount = 0;
        for(int i=start;i<end;i++){
            addRow(mean.get(i),(i+1)+"",word.get(i));
        }
        maxCheckedCount = end-start;
        if(checkedCount == maxCheckedCount){
            //모두가 체크되어 있으면
            checkbox_all.setChecked(true);
        }else{
            checkbox_all.setChecked(false);
        }
    }

    private void getPage(int page, int size){
        new JsonRequestUtil<EnglishDictionaryPageMaker>(context).request(
                Request.Method.GET,
                "/vocabulary/"+gno+"/"+vno+"?page="+page+"&size="+size,
                EnglishDictionaryPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    totalElements = response.getResult().getTotalElements();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<EnglishDictionary>>(){}.getType();
                    List<EnglishDictionary> list = new Gson().fromJson(json,listType);
                    for(int i=0;i<list.size();i++){
                        EnglishDictionary item = list.get(i);
                        word.add(item.getEnglishSpelling().getSpelling());
                        mean.add(item.getMeaning());
                    }
                    loading = false;
                    if(page == 1){
                        tv_table_num.setText(list.size()+"/"+totalElements);
                        if(totalPage == 1) {
                            //첫번째 페이지를 불러오는데 총 페이지수가 한개이면 > 즉 다음으로 가는 버튼은 보여서는 안된다.
                            btn_right.setVisibility(View.INVISIBLE);
                        }
                    }
                    addRows(PAGE_SIZE * (pageNum - 1),Math.min(PAGE_SIZE * pageNum, word.size()));
                },
                error -> {

                }
        );
    }

    private void addRow(String meaning,String num,String spelling){
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        TextView tv_num = new TextView(this);
        TextView tv_word = new TextView(this);
        TextView tv_mean = new TextView(this);
        CheckBox checkBox = new CheckBox(this);
        int id = Integer.parseInt(num);
        checkBox.setId(id);
        if(checkedIds.contains(id)){
            checkBox.setChecked(true);
            checkedCount++;
        }
        checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                checkedCount++;
                if(maxCheckedCount==checkedCount){
                    checkbox_all.setChecked(true);
                }
                myTable.insert(vno.intValue(),buttonView.getId());
                checkedIds.add(buttonView.getId());
            }else{
                checkedCount--;
                checkbox_all.setChecked(false);
                myTable.delete(vno.intValue(),buttonView.getId());
                checkedIds.remove(buttonView.getId());
            }
        }));
        tv_mean.setText(meaning);
        tv_num.setText(num);
        tv_num.setGravity(Gravity.CENTER);
        tv_word.setText(spelling);
        final float scale = context.getResources().getDisplayMetrics().density;
        tableRow.setBaselineAligned(false);//이걸 안 부르면 의미 컬럼의 글자가 잘림
        tableRow.addView(tv_num,new TableRow.LayoutParams((int)(30*scale), TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.addView(tv_word,new TableRow.LayoutParams((int)(100*scale), TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.addView(tv_mean,new TableRow.LayoutParams((int)(140*scale), TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.addView(checkBox);
        tableLayout.addView(tableRow);
    }
}