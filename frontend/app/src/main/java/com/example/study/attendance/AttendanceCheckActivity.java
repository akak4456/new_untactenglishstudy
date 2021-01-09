package com.example.study.attendance;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.attendance.adapter.AttendanceAdapter;
import com.example.study.attendance.adapter.AttendanceListAdapter;
import com.example.study.attendance.vo.Attendance;
import com.example.study.attendance.vo.AttendancePageMaker;
import com.example.study.board.adapter.BoardListAdapter;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.board.vo.BoardPageMaker;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AttendanceCheckActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    private AttendanceListAdapter adapter;
    private int pageNum;
    private int totalPage;
    private boolean loading;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;//스크롤 위치 저장
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_check);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        Button btn_attend = findViewById(R.id.btn_attend);
        //남은 출석 체크 시간 확보
        final TextView status = findViewById(R.id.status);
        final TextView tv_time = findViewById(R.id.tv_time);
        new JsonRequestUtil<Long>(context).request(
                Request.Method.GET,
                "/attendance/time/"+gno,
                Long.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    if(response >= 30){
                        status.setText("출석 가능");
                    }else if(response > 0){
                        status.setText("지각 가능");
                    }else{
                        status.setText("출석 체크 시간 아님");
                    }
                    Long hour = response/60;
                    Long minute = response%60;
                    tv_time.setText(String.format("%02d:%02d",hour,minute));
                },
                error->{

                }
        );
        btn_attend.setOnClickListener(view->{
            Activity activity = (Activity)context;
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/attendance/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        if(response.equals("attendance")){
                            Toast.makeText(context,"출석 체크됨", Toast.LENGTH_SHORT).show();
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                        }else if(response.equals("late")){
                            Toast.makeText(context,"지각", Toast.LENGTH_SHORT).show();
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                        }else if(response.equals("notaccept")){
                            Toast.makeText(context,"아직 출석체크 시간이 아님", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error-> {

                    }
            );
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        initRecyclerView();
        if(adapter != null)
            adapter.clearItems();
        adapter = new AttendanceListAdapter();
        pageNum = 2;
        loading = false;
        getPage(1,10);//일단 처음 페이지를 불러온다
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.attendance_list);
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
        new JsonRequestUtil<AttendancePageMaker>(context).request(
                Request.Method.GET,
                "/attendance/"+gno+"?page="+page+"&size="+size,
                AttendancePageMaker.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    totalPage = response.getTotalPages();
                    String json = new Gson().toJson(response.getResult().getContent());
                    Type listType = new TypeToken<List<Attendance>>(){}.getType();
                    List<Attendance> list = new Gson().fromJson(json,listType);
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
