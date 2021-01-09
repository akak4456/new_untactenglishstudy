package com.example.study.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.study.chat.vo.ChatResponse;
import com.example.study.global.MyMemberInfo;
import com.example.study.R;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatResponse> chatData = new ArrayList<>();

    public ChatAdapter(){

    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return chatData.size();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position){

        // 이름 일치하면 1(chat_others.xml)
        if(MyMemberInfo.getInstance().getMember().getMno().equals(chatData.get(position).getMno()))
            return 1;
        else // 불일치 0(chat_mychat.xml)
            return 0;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //항목의 index, 전에 inflate 되어있는 view, listView

        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            // Data Set(chatData)에서 position에 위치한 데이터 참조 획득
            ChatResponse listViewItem = chatData.get(position);

            switch (viewType) {
                case 0:
                    convertView = inflater.inflate(R.layout.chat_others,
                            parent, false);
                    TextView nameTextView = (TextView) convertView.findViewById(R.id.otherschat_tv_nickname) ;
                    TextView msgTextView = (TextView) convertView.findViewById(R.id.otherschat_tv_message) ;
                    TextView dateTextView = (TextView) convertView.findViewById(R.id.otherschat_tv_date) ;

                    nameTextView.setText(listViewItem.getFrom());
                    msgTextView.setText(listViewItem.getMsg());
                    dateTextView.setText(listViewItem.getTime());
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.chat_mychat,
                            parent, false);

                    TextView mymsgTextView = (TextView) convertView.findViewById(R.id.mychat_message) ;
                    TextView mydateTextView = (TextView) convertView.findViewById(R.id.mychat_date) ;

                    mymsgTextView.setText(listViewItem.getMsg());
                    mydateTextView.setText(listViewItem.getTime());
                    break;
            }
        }

        return convertView;



    }

    public void addItem(ChatResponse fromJson) {
        chatData.add(fromJson);
    }
}
