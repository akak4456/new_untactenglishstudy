package com.example.study.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.study.main.MainActivity;
import com.example.study.timetable.TimeTableListActivity;

import java.time.LocalDateTime;
import java.util.Calendar;

public class AlarmHATT {
    private static final String INTENT_ACTION = "arabiannight.tistory.com.alarmmanager";
    private Context context;
    public AlarmHATT(Context context){
        this.context = context;
    }
    public void setup(Long id,String title,String detail,int day,int startHour,int startMinute) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastD.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.putExtra("detail",detail);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id.intValue(), intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기
        switch(day){
            case 0:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                break;
            case 1:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
                break;
            case 2:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                break;
            case 3:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
                break;
            case 4:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                break;
            case 5:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                break;
            case 6:
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY,startHour);
        calendar.set(Calendar.MINUTE,startMinute);
        calendar.set(Calendar.SECOND,0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),7*24*60*60*1000, alarmIntent);
    }
    public void clearUp(Long id){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastD.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id.intValue(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.cancel(alarmIntent);
    }
}
