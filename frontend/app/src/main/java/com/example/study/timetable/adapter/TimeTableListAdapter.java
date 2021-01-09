package com.example.study.timetable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.alarm.AlarmHATT;
import com.example.study.global.CommonHeader;
import com.example.study.timetable.TimeTableListActivity;
import com.example.study.timetable.TimeTableOneActivity;
import com.example.study.timetable.vo.TimeTable;
import com.example.study.timetable.vo.TimeTableItem;
import com.example.study.timetable.vo.TimeTableVO;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimeTableListAdapter extends RecyclerView.Adapter<TimeTableListAdapter.ItemViewHolder> {
    private List<TimeTable> listData = new ArrayList<>();
    private Long gno;
    public TimeTableListAdapter(Long gno){
        this.gno = gno;
    }
    @NonNull
    @Override
    public TimeTableListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_list_item,parent,false);
        return new TimeTableListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableListAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<TimeTable> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private Button turnOff;
        private Button turnOn;
        ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            turnOff = itemView.findViewById(R.id.btn_turn_off);
            turnOn = itemView.findViewById(R.id.btn_turn_on);
            title.setOnClickListener(view -> {
                final Context context = view.getContext();
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    final TimeTable item = listData.get(pos);
                    Intent intent = new Intent(context, TimeTableOneActivity.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("gno",gno);
                    intent.putExtra("tno",item.getTno());
                    context.startActivity(intent);
                }
            });
            turnOff.setOnClickListener(view->{
                final Context context = view.getContext();
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    final TimeTable item = listData.get(pos);
                    new JsonRequestUtil<TimeTableVO>(context).request(
                            Request.Method.PUT,
                            "/timetable/toggle/"+gno+"/"+item.getTno(),
                            TimeTableVO.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            response->{
                                turnOff.setVisibility(View.GONE);
                                turnOn.setVisibility(View.VISIBLE);
                                String json = new Gson().toJson(response.getTimeTableItem());
                                Type listType = new TypeToken<List<TimeTableItem>>(){}.getType();
                                List<TimeTableItem> timeTableItem = new Gson().fromJson(json,listType);
                                for(TimeTableItem i:timeTableItem){
                                    new AlarmHATT(context).clearUp(i.getTino());
                                }
                            },
                            error->{

                            }
                    );

                }

            });
            turnOn.setOnClickListener(view->{
                final Context context = view.getContext();
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    final TimeTable item = listData.get(pos);
                    new JsonRequestUtil<TimeTableVO>(context).request(
                            Request.Method.PUT,
                            "/timetable/toggle/"+gno+"/"+item.getTno(),
                            TimeTableVO.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            response->{
                                turnOff.setVisibility(View.VISIBLE);
                                turnOn.setVisibility(View.GONE);
                                String json = new Gson().toJson(response.getTimeTableItem());
                                Type listType = new TypeToken<List<TimeTableItem>>(){}.getType();
                                List<TimeTableItem> timeTableItem = new Gson().fromJson(json,listType);
                                for(TimeTableItem i:timeTableItem){
                                    new AlarmHATT(context).setup(i.getTino(),i.getTitle(),i.getDetail(),i.getDay(),i.getStartHour(),i.getStartMinute());
                                }
                            },
                            error->{

                            }
                    );

                }
            });
        }
        void onBind(TimeTable data) {

            title.setText(data.getTitle());
            if(data.getIsAlarm().equals("Y")) {
                turnOff.setVisibility(View.VISIBLE);
                turnOn.setVisibility(View.GONE);
            }else{
                turnOff.setVisibility(View.GONE);
                turnOn.setVisibility(View.VISIBLE);
            }
        }
    }
}
