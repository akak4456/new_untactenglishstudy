package com.example.study.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.alarm.BroadcastD;
import com.example.study.global.CommonHeader;
import com.example.study.timetable.adapter.TimeTableListAdapter;
import com.example.study.timetable.vo.TimeTablePageMaker;
import com.example.study.timetable.vo.TimeTable;
import com.example.study.util.JsonRequestUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

public class TimeTableListActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private TimeTableListAdapter adapter;
    private int totalPage;//총페이지수
    private int pageNum;//현재 페이지수 + 1
    private Parcelable recyclerViewState;//스크롤 위치 저장
    private Long gno;
    private boolean loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_list);
        FloatingActionButton fab = findViewById(R.id.fab_upload);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(context, TimeTableAddActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            startActivity(intent);
        });
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
    }
    @Override
    public void onStart(){
        super.onStart();
        initView();
        getPage(1,10);
    }
    private void initView(){
        if(adapter != null)
            adapter.clearItems();
        adapter = new TimeTableListAdapter(gno);
        pageNum = 2;
        loading = false;

        recyclerView = findViewById(R.id.timetable_list);
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
        new JsonRequestUtil<TimeTablePageMaker>(context).request(
                Request.Method.GET,
                "/timetable/"+gno+"?page="+page+"&size="+size,
                TimeTablePageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<TimeTable>>(){}.getType();
                    List<TimeTable> list = new Gson().fromJson(json,listType);
                    //부른 데이터를 이용해서 recycler view를 바꾼 뒤에 recycler view가 맨 위로 갈 것인데 저장한 스크롤 위치로 되돌아간다
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
