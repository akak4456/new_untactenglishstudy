package com.example.study.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study.R;
import com.example.study.board.BoardOneActivity;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.member.vo.MemberEntity;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter {

    private List<BoardItemVO> noticeList;
    private Long gno;
    public NoticeListAdapter(List<BoardItemVO> noticeList,Long gno){
        this.noticeList = noticeList;
        this.gno = gno;
    }
    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notice_list_item,parent,false);
        }

        TextView title = convertView.findViewById(R.id.board_title);
        TextView author = convertView.findViewById(R.id.board_author);
        BoardItemVO item = noticeList.get(pos);
        title.setText(item.getTitle());
        author.setText(item.getMember().getName());

        convertView.setOnClickListener(v->{
            Intent intent = new Intent(context, BoardOneActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("gno",gno);
            intent.putExtra("bno",item.getBno());
            context.startActivity(intent);
        });
        return convertView;
    }
}
