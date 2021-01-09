package com.example.study.timetable;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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

import com.example.study.R;
import com.example.study.timetableview.Schedule;
import com.example.study.timetableview.Time;
import com.example.study.timetableview.TimetableView;

import java.util.ArrayList;

public class TimeTablePopup {
    private static final int minimum = 60;
    private Context context;
    private TimetableView timetable;
    private ArrayList<Schedule> schedules;
    public TimeTablePopup(Context context, TimetableView timetable,ArrayList<Schedule> schedules){
        this.context = context;
        this.timetable = timetable;
        this.schedules = schedules;
    }
    public void onButtonShowAddPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                view.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_timetable_add, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        EditText title = popupView.findViewById(R.id.title);
        EditText detail = popupView.findViewById(R.id.detail);

        Spinner daySpinner = popupView.findViewById(R.id.spinner_day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(context,R.array.day_list,android.R.layout.simple_spinner_item);
        daySpinner.setAdapter(dayAdapter);

        TimePicker startTime = popupView.findViewById(R.id.startTime);

        TimePicker endTime = popupView.findViewById(R.id.endTime);

        Button addItem = popupView.findViewById(R.id.add_item_btn);
        addItem.setOnClickListener(v->{
            /*
            시간표가 맞는지, 겹치는 스티커들(일정)은 없는지 확인
             */
            int pos = daySpinner.getSelectedItemPosition();
            int day = pos>=1&&pos<=7?pos-1:0;
            int startHour,endHour;
            int startMinute,endMinute;
            if(Build.VERSION.SDK_INT < 23) {
                startHour = startTime.getCurrentHour();
                startMinute = startTime.getCurrentMinute();
                endHour = endTime.getCurrentHour();
                endMinute = endTime.getCurrentMinute();
            }
            else {
                startHour = startTime.getHour();
                startMinute = startTime.getMinute();
                endHour = endTime.getHour();
                endMinute = endTime.getMinute();
            }
            if(!startTimeIsLessThenEndTime(startHour,startMinute,endHour,endMinute)){
                Toast.makeText(context, "시작 시간이 종료 시간보다 큽니다", Toast.LENGTH_SHORT).show();
                return;
            }
            if(checkOverlapTime(day,startHour,startMinute,endHour,endMinute)){
                Toast.makeText(context, "시간표가 겹칩니다", Toast.LENGTH_SHORT).show();
                return;
            }
            if(isLessThanMinute(startHour,startMinute,endHour,endMinute,minimum)){
                Toast.makeText(context, "시간표가 너무 짧습니다. 최소 "+minimum+"분 이상이어야 합니다", Toast.LENGTH_SHORT).show();
                return;
            }
            Schedule schedule = new Schedule();
            schedule.setTitle(title.getText().toString()); // sets subject
            schedule.setDetail(detail.getText().toString()); // sets place
            schedule.setDay(day);
            schedule.setStartTime(new Time(startHour,startMinute)); // sets the beginning of class time (hour,minute)
            schedule.setEndTime(new Time(endHour,endMinute)); // sets the end of class time (hour,minute)
            schedules.add(schedule);
            timetable.add(schedules);
            popupWindow.dismiss();
        });

        Button cancel = popupView.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(v->{
            popupWindow.dismiss();
        });
    }
    public void onButtonShowModifyPopupWindowClick(int idx) {

        // inflate the layout of the popup window
        Schedule oldSchedule = schedules.get(idx);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_timetable_modify, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(((Activity)context).findViewById(R.id.add_btn), Gravity.BOTTOM, 0, 0);

        EditText title = popupView.findViewById(R.id.title);
        title.setText(oldSchedule.getTitle());
        EditText detail = popupView.findViewById(R.id.detail);
        detail.setText(oldSchedule.getDetail());

        Spinner daySpinner = popupView.findViewById(R.id.spinner_day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(context,R.array.day_list,android.R.layout.simple_spinner_item);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setSelection(oldSchedule.getDay()+1);

        TimePicker startTime = popupView.findViewById(R.id.startTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startTime.setHour(oldSchedule.getStartTime().getHour());
            startTime.setMinute(oldSchedule.getStartTime().getMinute());
        }else{
            startTime.setCurrentHour(oldSchedule.getStartTime().getHour());
            startTime.setCurrentMinute(oldSchedule.getStartTime().getMinute());
        }
        TimePicker endTime = popupView.findViewById(R.id.endTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            endTime.setHour(oldSchedule.getEndTime().getHour());
            endTime.setMinute(oldSchedule.getEndTime().getMinute());
        }else{
            endTime.setCurrentHour(oldSchedule.getEndTime().getHour());
            endTime.setCurrentMinute(oldSchedule.getEndTime().getMinute());
        }
        Button modifyItem = popupView.findViewById(R.id.modify_item_btn);
        modifyItem.setOnClickListener(v->{
            /*
            시간표가 맞는지, 겹치는 스티커들(일정)은 없는지 확인
             */
            int pos = daySpinner.getSelectedItemPosition();
            int day = pos>=1&&pos<=7?pos-1:0;
            int startHour,endHour;
            int startMinute,endMinute;
            if(Build.VERSION.SDK_INT < 23) {
                startHour = startTime.getCurrentHour();
                startMinute = startTime.getCurrentMinute();
                endHour = endTime.getCurrentHour();
                endMinute = endTime.getCurrentMinute();
            }
            else {
                startHour = startTime.getHour();
                startMinute = startTime.getMinute();
                endHour = endTime.getHour();
                endMinute = endTime.getMinute();
            }
            if(!startTimeIsLessThenEndTime(startHour,startMinute,endHour,endMinute)){
                Toast.makeText(context, "시작 시간이 종료 시간보다 큽니다", Toast.LENGTH_SHORT).show();
                return;
            }
            if(checkOverlapTime(day,startHour,startMinute,endHour,endMinute,idx)){
                Toast.makeText(context, "시간표가 겹칩니다", Toast.LENGTH_SHORT).show();
                return;
            }
            if(isLessThanMinute(startHour,startMinute,endHour,endMinute,minimum)){
                Toast.makeText(context, "시간표가 너무 짧습니다. 최소 "+minimum+"분 이상이어야 합니다", Toast.LENGTH_SHORT).show();
                return;
            }
            Schedule schedule = new Schedule();
            schedule.setTitle(title.getText().toString()); // sets subject
            schedule.setDetail(detail.getText().toString()); // sets place
            schedule.setDay(day);
            schedule.setStartTime(new Time(startHour,startMinute)); // sets the beginning of class time (hour,minute)
            schedule.setEndTime(new Time(endHour,endMinute)); // sets the end of class time (hour,minute)
            schedules.set(idx,schedule);
            timetable.edit(idx,schedules);
            popupWindow.dismiss();
        });

        Button cancel = popupView.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(v->{
            popupWindow.dismiss();
        });

    }
    private boolean startTimeIsLessThenEndTime(int startHour,int startMinute,int endHour,int endMinute){
        Time startTime = new Time(startHour,startMinute);
        Time endTime = new Time(endHour,endMinute);
        return startTime.isLessThan(endTime);
    }
    private boolean checkOverlapTime(int day,int startHour, int startMinute,int endHour, int endMinute){
        return checkOverlapTime(day,startHour,startMinute,endHour,endMinute,-1);
    }
    private boolean isLessThanMinute(int startHour, int startMinute,int endHour,int endMinute,int targetMinutes){
        int minutes = (endHour-startHour)*60+(endMinute-startMinute);
        return minutes < targetMinutes;
    }
    private boolean checkOverlapTime(int day,int startHour, int startMinute,int endHour,int endMinute,int skipIdx){
        for(int i=0;i<schedules.size();i++){
            if(skipIdx != -1&&i==skipIdx){
                continue;
            }
            Schedule cur = schedules.get(i);
            if(cur.getDay() == day){
                //bool overlap = tStartA < tEndB && tStartB < tEndA;
                Time tStartA = cur.getStartTime();
                Time tEndA = cur.getEndTime();
                Time tStartB = new Time(startHour,startMinute);
                Time tEndB = new Time(endHour,endMinute);
                if(tStartA.isLessThan(tEndB)&&tStartB.isLessThan(tEndA)){
                    return true;
                }
            }
        }
        return false;
    }
}
