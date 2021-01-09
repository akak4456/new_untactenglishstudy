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
import com.example.study.study.CreateGroupActivity;
import com.example.study.timetable.vo.TimeTable;
import com.example.study.timetable.vo.TimeTableItem;
import com.example.study.timetable.vo.TimeTableVO;
import com.example.study.timetableview.Schedule;
import com.example.study.timetableview.Time;
import com.example.study.timetableview.TimetableView;
import com.example.study.util.JsonRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class TimeTableAddActivity extends AppCompatActivity {
    private ArrayList<Schedule> schedules;
    private Context context;
    private TimetableView timetable;
    private Long gno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_add);
        context = TimeTableAddActivity.this;
        timetable = findViewById(R.id.timetable);
        timetable.post(()->{
            timetable.scrollToByY(1500);
        });
        schedules = new ArrayList<>();
        gno = getIntent().getLongExtra("gno",0);
        EditText title = findViewById(R.id.title);
        Button add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(view->{
            new TimeTablePopup(context,timetable,schedules).onButtonShowAddPopupWindowClick(view);
        });
        Button save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(view->{
            TimeTable timeTable = new TimeTable();
            timeTable.setTitle(title.getText().toString());
            timeTable.setIsAlarm("N");
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
                    Request.Method.POST,
                    "/timetable/"+gno,
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new TimeTableVO(timeTable,timeTableItems),
                    response->{
                        Toast.makeText(context,"성공", Toast.LENGTH_SHORT).show();
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
