package com.example.study.attendance.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;
import com.example.study.attendance.vo.Attendance;
import com.example.study.board.BoardOneActivity;
import com.example.study.board.vo.BoardItemVO;

import java.util.ArrayList;
import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ItemViewHolder> {
    private List<Attendance> listData = new ArrayList<>();
    public AttendanceListAdapter(){

    }
    @NonNull
    @Override
    public AttendanceListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item,parent,false);
        return new AttendanceListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<Attendance> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView status;
        private TextView regdate;

        ItemViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            regdate = itemView.findViewById(R.id.regdate);
        }

        void onBind(Attendance data) {
            if(data.getStatus().equals("OK")){
                status.setText("출석");
            }else if(data.getStatus().equals("LATE")){
                status.setText("지각");
            }else{
                status.setText("결석");
            }
            regdate.setText(data.getRegdate());
        }
    }
}
