package com.example.study.attendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.example.study.R;
import com.example.study.study.vo.GroupMemVO;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AttendanceAdapter extends BaseAdapter {

    private TextView textView;
    private CheckBox checkBox;
    private TextView time;

    private ArrayList<GroupMemVO> groupMemVOArrayList = new ArrayList<GroupMemVO>();

    public AttendanceAdapter(){

    }

    @Override
    public int getCount(){
        return groupMemVOArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.attendance_listview_item,parent,false);

        }
        textView = (TextView) convertView.findViewById(R.id.name);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        time = (TextView) convertView.findViewById(R.id.time);

        GroupMemVO groupMemVO = groupMemVOArrayList.get(position);

        textView.setText(groupMemVO.getName());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return groupMemVOArrayList.get(position);
    }


    public void onClick(View v){
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatDate = simpleDateFormat.format(mDate);


        checkBox.setChecked(true);
        time.setText(formatDate);
    }


    public void addItem(String name){
        GroupMemVO item = new GroupMemVO();
        item.setName(name);
        groupMemVOArrayList.add(item);
    }

}
