package com.example.study.leader;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.leader.adapter.AttendanceManageAdapter;
import com.example.study.leader.adapter.MemberManageAdapter;
import com.example.study.leader.vo.AttendanceResponse;
import com.example.study.leader.vo.AttendanceVO;
import com.example.study.leader.vo.MemberManageResponse;
import com.example.study.leader.vo.MemberManageVO;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;


public class AttendanceManageActivity extends AppCompatActivity {
    private Context context;
    private ListView ll;
    private Long gno;
    private Calendar time;
    TextView tv_date;
    Button btn_left;
    Button btn_right;
    private AttendanceManageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_manage);
        context = this;
        ll = findViewById(R.id.attendance_manage_list);
        gno = getIntent().getLongExtra("gno",0L);
        time = Calendar.getInstance();
        btn_left = findViewById(R.id.btn_left);
        btn_left.setOnClickListener(view->{
            System.out.println(time.toString());
            time.add(Calendar.DATE,-1);
            setUpView();
        });
        tv_date = findViewById(R.id.tv_date);
        btn_right = findViewById(R.id.btn_right);
        btn_right.setOnClickListener(view->{
            time.add(Calendar.DATE,1);
            setUpView();
        });
        setUpView();
    }

    private void setUpView(){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String curTime = format1.format(time.getTime());
        tv_date.setText(curTime);
        String nowTime = format1.format(Calendar.getInstance().getTime());
        if(curTime.equals(nowTime)){
            btn_right.setVisibility(View.INVISIBLE);
        }else{
            btn_right.setVisibility(View.VISIBLE);
        }

        new JsonRequestUtil<AttendanceResponse>(context).request(
                Request.Method.GET,
                "/leader/attendance/"+gno+"/"+curTime,
                AttendanceResponse.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    String json = new Gson().toJson(response.getAttendances());
                    Type listType = new TypeToken<List<AttendanceVO>>(){}.getType();
                    List<AttendanceVO> list = new Gson().fromJson(json,listType);
                    adapter = new AttendanceManageAdapter(list,gno);
                    ll.setAdapter(adapter);
                },
                error->{
                    Toast.makeText(context,"읽을 수 없음",Toast.LENGTH_SHORT).show();
                }
        );
    }
}
