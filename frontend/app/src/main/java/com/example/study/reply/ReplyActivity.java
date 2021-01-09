package com.example.study.reply;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.board.adapter.BoardListAdapter;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.board.vo.BoardPageMaker;
import com.example.study.chat.adapter.ChatAdapter;
import com.example.study.chat.vo.ChatRequest;
import com.example.study.chat.vo.ChatResponse;
import com.example.study.constant.Constant;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.member.vo.MemberEntity;
import com.example.study.reply.adapter.ReplyAdapter;
import com.example.study.reply.vo.Reply;
import com.example.study.reply.vo.ReplyPageMaker;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ReplyActivity extends AppCompatActivity {

    private Context context;

    private Long gno;

    private Long bno;

    private ListView lv;

    private ReplyAdapter adapter;

    private int pageNum;

    private boolean loading;

    private RecyclerView recyclerView;

    private Parcelable recyclerViewState;

    private int totalPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        bno = getIntent().getLongExtra("bno",0L);
        EditText edit_text = findViewById(R.id.edit_text);
        Button button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(view-> {
            new JsonRequestUtil<String>(context).request(
                    Request.Method.POST,
                    "/reply/" + gno + "/" + bno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new Reply(edit_text.getText().toString()),
                    response -> {
                        Toast.makeText(context, "댓글을 달았습니다", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    },
                    error -> {

                    }
            );
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        initView();
    }
    private void initView(){
        if(adapter != null)
            adapter.clearItems();
        adapter = new ReplyAdapter();
        pageNum = 2;
        loading = false;

        recyclerView = findViewById(R.id.reply_list);
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

        getPage(1,10);
    }

    private void getPage(int page, int size){
        new JsonRequestUtil<ReplyPageMaker>(context).request(
                Request.Method.GET,
                "/reply/"+gno+"/"+bno+"/?page="+page+"&size="+size,
                ReplyPageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<Reply>>(){}.getType();
                    List<Reply> list = new Gson().fromJson(json,listType);
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
