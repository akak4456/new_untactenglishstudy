package com.example.study.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.study.board.adapter.BoardListAdapter;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.board.vo.BoardPageMaker;
import com.example.study.global.CommonHeader;
import com.example.study.R;
import com.example.study.util.JsonRequestUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BoardListActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;
    private BoardListAdapter adapter;
    private int totalPage;//총페이지수
    private int pageNum;//현재 페이지수 + 1
    private Parcelable recyclerViewState;//스크롤 위치 저장
    private Long gno;
    private boolean loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        FloatingActionButton fab = findViewById(R.id.fab_upload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시글 추가 페이지로 이동
                Intent intent = new Intent(context, BoardAddActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("gno",gno);
                intent.putExtra("kind","NORMAL");
                startActivity(intent);
            }
        });
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
    }
    @Override
    public void onStart(){
        super.onStart();
        initRecyclerView();
        if(adapter != null)
            adapter.clearItems();
        adapter = new BoardListAdapter(gno);
        pageNum = 2;
        loading = false;
        getPage(1,10);//일단 처음 페이지를 불러온다
    }
    private void initRecyclerView(){
        recyclerView = findViewById(R.id.board_list);
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
        new JsonRequestUtil<BoardPageMaker>(context).request(
                Request.Method.GET,
                "/board/"+gno+"?page="+page+"&size="+size,
                BoardPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<BoardItemVO>>(){}.getType();
                    List<BoardItemVO> list = new Gson().fromJson(json,listType);
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
