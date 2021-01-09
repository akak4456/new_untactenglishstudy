package com.example.study.timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.board.BoardModifyActivity;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.board.vo.BoardVO;
import com.example.study.global.CommonHeader;
import com.example.study.timetable.vo.TimeTable;
import com.example.study.timetable.vo.TimeTableItem;
import com.example.study.timetable.vo.TimeTableVO;
import com.example.study.timetableview.Schedule;
import com.example.study.timetableview.Time;
import com.example.study.timetableview.TimetableView;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimeTableOneActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    private Long tno;
    private TimetableView timetable;
    private ArrayList<Schedule> schedules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_one);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        tno = getIntent().getLongExtra("tno",0L);
        timetable = findViewById(R.id.timetable);
        timetable.post(()->{
            timetable.scrollToByY(1500);
        });
        schedules = new ArrayList<>();
        Button modify_btn = findViewById(R.id.modify_btn);
        modify_btn.setOnClickListener(view->{
            Intent intent = new Intent(context, TimeTableModifyActivity.class );
            intent.putExtra("gno",gno);
            intent.putExtra("tno",tno);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        Button delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(view -> {
            new JsonRequestUtil<String>(context).request(
                    Request.Method.DELETE,
                    "/timetable/"+gno+"/"+tno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        Toast.makeText(context,"성공", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {

                    }

            );
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        TextView title = findViewById(R.id.title);

        timetable.removeAll();
        schedules.clear();
        new JsonRequestUtil<TimeTableVO>(context).request(
                Request.Method.GET,
                "/timetable/"+gno+"/"+tno,
                TimeTableVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    TimeTable timeTable = response.getTimeTable();
                    String json = new Gson().toJson(response.getTimeTableItem());
                    Type listType = new TypeToken<List<TimeTableItem>>(){}.getType();
                    List<TimeTableItem> timeTableItem = new Gson().fromJson(json,listType);
                    for(TimeTableItem item:timeTableItem){
                        Schedule schedule = new Schedule();
                        schedule.setTitle(item.getTitle());
                        schedule.setDetail(item.getDetail());
                        schedule.setDay(item.getDay());
                        Time startTime = new Time(item.getStartHour(),item.getStartMinute());
                        schedule.setStartTime(startTime);
                        Time endTime = new Time(item.getEndHour(),item.getEndMinute());
                        schedule.setEndTime(endTime);
                        schedules.add(schedule);
                    }
                    title.setText(timeTable.getTitle());
                    timetable.add(schedules);
                },
                error -> {

                }
        );
    }
}
