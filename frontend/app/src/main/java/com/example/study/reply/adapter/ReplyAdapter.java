package com.example.study.reply.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.board.BoardOneActivity;
import com.example.study.board.adapter.BoardListAdapter;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.chat.vo.ChatResponse;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.reply.vo.Reply;
import com.example.study.util.JsonRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Reply> listData = new ArrayList<>();
    class ViewHolderMy extends RecyclerView.ViewHolder{
        private TextView date;
        //공통으로 보여줘야 할 부분
        private TextView message;
        private LinearLayout btn_layout_1;
        private Button show_modify_btn;
        private Button delete_btn;
        //수정창이 안열렸을 때 보여줘야 할 부분
        private EditText edit_message;
        private LinearLayout btn_layout_2;
        private Button modify_btn;
        private Button cancel_btn;
        //수정창이 열렸을 때 보여줘야 할 부분
        private boolean modifyMode = false;
        public ViewHolderMy(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
            edit_message = itemView.findViewById(R.id.edit_message);
            btn_layout_1 = itemView.findViewById(R.id.btn_layout_1);
            btn_layout_2 = itemView.findViewById(R.id.btn_layout_2);
            edit_message.setVisibility(View.GONE);
            btn_layout_2.setVisibility(View.GONE);
            show_modify_btn = itemView.findViewById(R.id.show_modify_btn);
            show_modify_btn.setOnClickListener(view->{
                toggleMode();
            });
            delete_btn = itemView.findViewById(R.id.delete_btn);
            delete_btn.setOnClickListener(v->{
                final Context context = v.getContext();
                final Activity activity = (Activity) context;
                int pos = getAdapterPosition();
                Long rno = listData.get(pos).getRno();
                Long mno = MyMemberInfo.getInstance().getMember().getMno();
                if(pos != RecyclerView.NO_POSITION){
                    new JsonRequestUtil<String>(context).request(
                            Request.Method.DELETE,
                            "/reply/"+rno+"/"+mno,
                            String.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            response->{
                                Toast.makeText(context, "댓글을 삭제했습니다", Toast.LENGTH_SHORT).show();
                                activity.finish();
                                activity.startActivity(activity.getIntent());
                            },
                            error->{

                            }
                    );
                }
            });
            modify_btn = itemView.findViewById(R.id.modify_btn);
            modify_btn.setOnClickListener(v->{
                final Context context = v.getContext();
                int pos = getAdapterPosition();
                Long rno = listData.get(pos).getRno();
                Long mno = MyMemberInfo.getInstance().getMember().getMno();
                if(pos != RecyclerView.NO_POSITION){
                    new JsonRequestUtil<String>(context).request(
                            Request.Method.PUT,
                            "/reply/"+rno+"/"+mno,
                            String.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            new Reply(edit_message.getText().toString()),
                            response->{
                                Toast.makeText(context, "댓글을 수정했습니다", Toast.LENGTH_SHORT).show();
                                toggleMode();
                                message.setText(edit_message.getText().toString());//취소할 때도 toggleMode를 쓰니
                                //수정이 완료되기 전까지는 message를 만지면 안됨!!!
                            },
                            error->{

                            }
                    );
                }
            });
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
            cancel_btn.setOnClickListener(v->{
                toggleMode();
            });
        }
        public void toggleMode(){
            if(!modifyMode){
                //수정상태가 아니라면
                message.setVisibility(View.GONE);
                btn_layout_1.setVisibility(View.GONE);
                edit_message.setVisibility(View.VISIBLE);
                btn_layout_2.setVisibility(View.VISIBLE);
                //수정 상태로 바뀌어라
                edit_message.setText(message.getText().toString());
            }else{
                message.setVisibility(View.VISIBLE);
                btn_layout_1.setVisibility(View.VISIBLE);
                edit_message.setVisibility(View.GONE);
                btn_layout_2.setVisibility(View.GONE);
            }

            modifyMode = !modifyMode;
        }
        public void onBind(Reply reply){
            message.setText(reply.getMessage());
            date.setText(reply.getUpdatedate());
        }
    }

    class ViewHolderOthers extends RecyclerView.ViewHolder{
        private TextView nickname;
        private TextView message;
        private TextView date;
        public ViewHolderOthers(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }

        public void onBind(Reply reply){
            nickname.setText(reply.getMember().getName());
            message.setText(reply.getMessage());
            date.setText(reply.getUpdatedate());
        }
    }

    @Override
    public int getItemViewType(int position){
        if(MyMemberInfo.getInstance().getMember().getMno().equals(listData.get(position).getMember().getMno()))
            return 1;
        else // 불일치 0(chat_mychat.xml)
            return 0;
    }
    public ReplyAdapter(){

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 0)
        {
            view = inflater.inflate(R.layout.reply_others, parent, false);
            return new ViewHolderOthers(view);
        }
        else if(viewType == 1)
        {
            view = inflater.inflate(R.layout.reply_my, parent, false);
            return new ViewHolderMy(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder instanceof ViewHolderOthers)
        {
            ((ViewHolderOthers) viewHolder).onBind(listData.get(position));
        }
        else if(viewHolder instanceof ViewHolderMy)
        {
            ((ViewHolderMy) viewHolder).onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<Reply> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

}
