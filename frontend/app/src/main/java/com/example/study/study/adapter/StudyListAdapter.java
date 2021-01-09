package com.example.study.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.study.study.MyStudyActivity;
import com.example.study.study.vo.GroupItemVO;
import com.example.study.global.CommonHeader;
import com.example.study.R;
import com.example.study.util.JsonRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class StudyListAdapter extends RecyclerView.Adapter<StudyListAdapter.ItemViewHolder> {
    private List<GroupItemVO> listData = new ArrayList<>();
    @NonNull
    @Override
    public StudyListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyListAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItems(List<GroupItemVO> list){
        listData.addAll(list);
    }

    public void clearItems(){listData.clear();}

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView detail;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                final Context context = view.getContext();
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    final GroupItemVO item = listData.get(pos);
                    new JsonRequestUtil<String>(context).request(
                            Request.Method.GET,
                            "/study/try/"+item.getGno(),
                            String.class,
                            CommonHeader.getInstance().getCommonHeader(),
                            response -> {
                                //자세한 응답메시지 규약은 REST API 설계서 참고
                                if(response.equals("first")){
                                    showDialog(context,item);
                                }else if(response.equals("wait")){
                                    Toast.makeText(context,"심사 중입니다. 기다려주세요", Toast.LENGTH_SHORT).show();
                                }else if(response.equals("denied")){
                                    Toast.makeText(context,"거절되었습니다.", Toast.LENGTH_SHORT).show();
                                }else if(response.equals("success")){
                                    Intent intent = new Intent(context, MyStudyActivity.class );
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("gno",item.getGno());
                                    context.startActivity(intent);
                                }else if(response.equals("full")){
                                    Toast.makeText(context,"사람이 다 찾습니다. 다른 스터디를 찾아주세요", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> {

                            }
                    );

                }
            });
            title = itemView.findViewById(R.id.studyTitle);
            detail = itemView.findViewById(R.id.studyDetail);
        }

        private void showDialog(final Context context, final GroupItemVO item){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("스터디 가입");
            builder.setMessage("제목:"+item.getTitle()+"\n상세:"+item.getDetail());
            builder.setPositiveButton("그룹 가입",(dialog,which) -> {
                new JsonRequestUtil<String>(context).request(
                        Request.Method.POST,
                        "/waiting/"+item.getGno(),
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response -> Toast.makeText(context, "그룹가입을 신청하셨습니다. 그룹에서 승인할 때까지 기다려주세요", Toast.LENGTH_LONG).show(),
                        error -> Toast.makeText(context,error.getMessage(), Toast.LENGTH_SHORT).show()
                );

                dialog.dismiss();
            });
            builder.setNegativeButton("취소",(dialog,which)->{
                dialog.dismiss();
            });
            builder.show();
        }

        void onBind(GroupItemVO data) {
            title.setText(data.getTitle());
            detail.setText(data.getDetail());
        }
    }

}
