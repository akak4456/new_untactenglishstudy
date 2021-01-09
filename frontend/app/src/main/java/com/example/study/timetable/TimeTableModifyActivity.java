package com.example.study.timetable;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
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

public class TimeTableModifyActivity extends AppCompatActivity {
    private ArrayList<Schedule> schedules;
    private Context context;
    private TimetableView timetable;
    private Long gno;
    private Long tno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_modify);
        context = this;
        timetable = findViewById(R.id.timetable);
        timetable.post(()->{
            timetable.scrollToByY(1500);
        });
        schedules = new ArrayList<>();
        gno = getIntent().getLongExtra("gno",0L);
        tno = getIntent().getLongExtra("tno",0L);
        EditText title = findViewById(R.id.title);
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
        Button add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(view->{
            new TimeTablePopup(context,timetable,schedules).onButtonShowAddPopupWindowClick(view);
        });
        Button save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(view->{
            TimeTable timeTable = new TimeTable();
            timeTable.setTitle(title.getText().toString());
            List<TimeTableItem> timeTableItems = new ArrayList<>();
            for(Schedule s:schedules){
                TimeTableItem timeTableItem = new TimeTableItem();
                timeTableItem.setTitle(s.getTitle());
                timeTableItem.setDetail(s.getDetail());
                timeTableItem.setDay(s.getDay());
                timeTableItem.setStartHour(s.getStartTime().getHour());
                timeTableItem.setStartMinute(s.getStartTime().getMinute());
                timeTableItem.setEndHour(s.getEndTime().getHour());
                timeTableItem.setEndMinute(s.getEndTime().getMinute());
                timeTableItems.add(timeTableItem);
            }
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/timetable/"+gno+"/"+tno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new TimeTableVO(timeTable,timeTableItems),
                    response->{
                        Toast.makeText(context,"변경되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error->{

                    }
            );
        });
        timetable.setOnStickerSelectEventListener((idx, sche) -> {
            new TimeTablePopup(context,timetable,schedules).onButtonShowModifyPopupWindowClick(idx);
        });
    }
}
