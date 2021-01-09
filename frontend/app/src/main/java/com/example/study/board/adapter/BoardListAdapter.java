package com.example.study.board.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.board.BoardOneActivity;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.R;

import java.util.ArrayList;
import java.util.List;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.ItemViewHolder> {
    private List<BoardItemVO> listData = new ArrayList<>();
    private Long gno;
    public BoardListAdapter(Long gno){
        this.gno = gno;
    }
    @NonNull
    @Override
    public BoardListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list_item,parent,false);
        return new BoardListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardListAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<BoardItemVO> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView author;

        ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        BoardItemVO item = listData.get(pos);
                        Intent intent = new Intent(context, BoardOneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("gno",gno);
                        intent.putExtra("bno",item.getBno());
                        context.startActivity(intent);
                    }
                }
            });

            title = itemView.findViewById(R.id.board_title);
            author = itemView.findViewById(R.id.board_author);
        }

        void onBind(BoardItemVO data) {
            if(data.getKind().equals("NORMAL")) {
                title.setText("[일반]  "+data.getTitle());
            }else if(data.getKind().equals("NOTICE")) {
                title.setText("[공지]  "+data.getTitle());
            }
            author.setText(data.getMember().getName());
        }
    }
}
