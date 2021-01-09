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
import com.example.study.global.CommonHeader;
import com.example.study.timetable.vo.RepresentativeTimeTableVO;
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

public class RepresentativeTimeTableOneActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    private TimetableView timetable;
    private ArrayList<Schedule> schedules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_timetable_one);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        timetable = findViewById(R.id.timetable);
        timetable.post(()->{
            timetable.scrollToByY(1500);
        });
        schedules = new ArrayList<>();
        Button modify_btn = findViewById(R.id.modify_btn);
        modify_btn.setOnClickListener(view->{
            Intent intent = new Intent(context, RepresentativeTimeTableModifyActivity.class );
            intent.putExtra("gno",gno);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        TextView title = findViewById(R.id.title);
        new JsonRequestUtil<RepresentativeTimeTableVO>(context).request(
                Request.Method.GET,
                "/timetable/represent/"+gno,
                RepresentativeTimeTableVO.class,
                CommonHeader.getInstance().getCommonHeader(),
                response -> {
                    String json = new Gson().toJson(response.getTimeTableItem());
                    Type listType = new TypeToken<List<TimeTableItem>>(){}.getType();
                    List<TimeTableItem> timeTableItem = new Gson().fromJson(json,listType);
                    if(response.getTitle() != null) {
                        for (TimeTableItem item : timeTableItem) {
                            Schedule schedule = new Schedule();
                            schedule.setTitle(item.getTitle());
                            schedule.setDetail(item.getDetail());
                            schedule.setDay(item.getDay());
                            Time startTime = new Time(item.getStartHour(), item.getStartMinute());
                            schedule.setStartTime(startTime);
                            Time endTime = new Time(item.getEndHour(), item.getEndMinute());
                            schedule.setEndTime(endTime);
                            schedules.add(schedule);
                        }
                        title.setText(response.getTitle());
                        timetable.add(schedules);
                    }
                },
                error -> {

                }
        );
    }
}
