package com.example.study.engcontent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study.R;

import java.util.ArrayList;

public class VocaAddAdapter extends BaseAdapter {

    private Context mContext;
    private TextView pathTextView;
    private ArrayList<String> array_path;

    public VocaAddAdapter(Context mContext, ArrayList<String> array_path){
        this.mContext = mContext;
        this.array_path = array_path;
    }

    @Override
    public int getCount(){
        return array_path.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.device_storage_item,parent,false);
        }

        pathTextView = (TextView) convertView.findViewById(R.id.tv_path);

        pathTextView.setText(array_path.get(position));

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return array_path.get(position);
    }



}
