package com.example.study.ranking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study.R;
import com.example.study.member.vo.MemberEntity;

import org.w3c.dom.Text;

import java.util.List;

public class RankMemberListAdapter extends BaseAdapter {
    private List<MemberEntity> memberList;
    public RankMemberListAdapter(List<MemberEntity> set){
        memberList = set;
    }
    TextView name;
    TextView pos_name;//직위가 무엇인지
    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int i) {
        return memberList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_wait_list_item,parent,false);

        }
        name = (TextView) convertView.findViewById(R.id.name);
        MemberEntity member = memberList.get(pos);
        name.setText(member.getName());
        return convertView;
    }
}
