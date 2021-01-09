package com.example.study.engcontent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;
import com.example.study.board.BoardOneActivity;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.engcontent.activity.VocaCardActivity;
import com.example.study.engcontent.vo.VocaItem;

import java.util.ArrayList;
import java.util.List;

public class VocaListAdapter extends RecyclerView.Adapter<VocaListAdapter.ItemViewHolder> {
    private List<VocaItem> listData = new ArrayList<>();
    private Long gno;
    public VocaListAdapter(Long gno){
        this.gno = gno;
    }
    @NonNull
    @Override
    public VocaListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voca_list_item,parent,false);
        return new VocaListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaListAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<VocaItem> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView voca_title;
        private TextView voca_num;

        ItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        VocaItem item = listData.get(pos);
                        //item click할때
                        Intent intent = new Intent(context, VocaCardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("gno",gno);
                        intent.putExtra("vno",item.getVno());
                        context.startActivity(intent);
                    }
                }
            });

            voca_title = itemView.findViewById(R.id.voca_title);
            voca_num = itemView.findViewById(R.id.voca_num);
        }

        void onBind(VocaItem data) {
            voca_title.setText(data.getTitle());
            voca_num.setText(data.getCount()+"");
        }
    }
}
