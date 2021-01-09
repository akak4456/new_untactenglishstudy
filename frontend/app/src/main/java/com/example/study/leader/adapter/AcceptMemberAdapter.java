package com.example.study.leader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.leader.AttendanceManageActivity;
import com.example.study.leader.vo.AttendanceVO;
import com.example.study.leader.vo.GroupInclude;
import com.example.study.util.JsonRequestUtil;

import java.util.List;

public class AcceptMemberAdapter extends BaseAdapter {
    private List<GroupInclude> data;
    private Long gno;
    public AcceptMemberAdapter(List<GroupInclude> list, Long gno){
        data = list; this.gno = gno;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GroupInclude getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        final Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.accept_member_item, null);

        GroupInclude item = data.get(position);

        TextView name = view.findViewById(R.id.name);
        name.setText(item.getMember().getName());
        Button accept = view.findViewById(R.id.accept);
        accept.setOnClickListener(v->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/waiting/"+gno+"/"+item.getGino()+"/accept",
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        Toast.makeText(context,"승인이 완료되었습니다",Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                    },
                    error->{
                        Toast.makeText(context,"승인이 안되었습니다",Toast.LENGTH_SHORT).show();
                    }
            );
        });
        Button reject = view.findViewById(R.id.reject);
        reject.setOnClickListener(v->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/waiting/"+gno+"/"+item.getGino()+"/reject",
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        Toast.makeText(context,"거절이 완료되었습니다",Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                    },
                    error->{
                        Toast.makeText(context,"거절이 안되었습니다",Toast.LENGTH_SHORT).show();
                    }
            );
        });
        return view;
    }
    public void clearItems(){
        data.clear();
        notifyDataSetChanged();
    }
}
