package com.example.study.leader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.chat.vo.ChatResponse;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.leader.MemberManageActivity;
import com.example.study.leader.vo.MemberManageVO;
import com.example.study.main.MainActivity;
import com.example.study.member.LoginActivity;
import com.example.study.util.JsonRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class MemberManageAdapter extends BaseAdapter {
    private List<MemberManageVO> data;
    private Long gno;
    public MemberManageAdapter(List<MemberManageVO> list,Long gno){
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
    public MemberManageVO getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        final Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.activity_member_manage_item, null);

        TextView name = view.findViewById(R.id.name);
        TextView attendance = view.findViewById(R.id.attendance);
        TextView absent = view.findViewById(R.id.absent);
        TextView late = view.findViewById(R.id.late);
        TextView deposit = view.findViewById(R.id.deposit);
        TextView fine = view.findViewById(R.id.fine);
        TextView reward = view.findViewById(R.id.reward);

        MemberManageVO item = data.get(position);

        name.setText(item.getName());
        attendance.setText(item.getAttendance()+"번");
        absent.setText(item.getAbsent()+"번");
        late.setText(item.getLate()+"번");
        deposit.setText(item.getDeposit()+"원");
        fine.setText(item.getFine()+"원");
        reward.setText(item.getReward()+"원");

        Button btn_change_reward = view.findViewById(R.id.btn_change_reward);
        btn_change_reward.setOnClickListener(v->{
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("상금 조정");
            ad.setMessage("조정할 상금을 써주세요");
            final EditText et = new EditText(context);
            et.setText(item.getReward()+"");
            ad.setView(et);
            ad.setPositiveButton("확인", (dialog, which) -> {
                Long newAmount = Long.parseLong(et.getText().toString());
                new JsonRequestUtil<String>(context).request(
                        Request.Method.PUT,
                        "/leader/reward/"+gno+"/"+item.getMno()+"/"+newAmount,
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response->{
                            Toast.makeText(context,"상금 조정 성공",Toast.LENGTH_SHORT).show();
                            reload(context);
                        },
                        error->{
                            Toast.makeText(context,"상금 조정 실패",Toast.LENGTH_SHORT).show();
                        }
                );
                dialog.dismiss();
            });
            ad.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            ad.show();
        });

        Button btn_change_fine = view.findViewById(R.id.btn_change_fine);
        btn_change_fine.setOnClickListener(v->{
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("벌금 조정");
            ad.setMessage("조정할 벌금을 써주세요");
            final EditText et = new EditText(context);
            et.setText(item.getFine()+"");
            ad.setView(et);
            ad.setPositiveButton("확인", (dialog, which) -> {
                Long newAmount = Long.parseLong(et.getText().toString());
                new JsonRequestUtil<String>(context).request(
                        Request.Method.PUT,
                        "/leader/fine/"+gno+"/"+item.getMno()+"/"+newAmount,
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response->{
                            Toast.makeText(context,"벌금 조정 성공",Toast.LENGTH_SHORT).show();
                            reload(context);
                        },
                        error->{
                            Toast.makeText(context,"벌금 조정 실패",Toast.LENGTH_SHORT).show();
                        }
                );
                dialog.dismiss();
            });
            ad.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            ad.show();
        });
        Button btn_eject = view.findViewById(R.id.btn_eject);
        btn_eject.setOnClickListener(v->{
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("강제 추방");
            ad.setMessage("정말로 강제 추방 하시겠습니까?");
            ad.setPositiveButton("확인", (dialog, which) -> {
                new JsonRequestUtil<String>(context).request(
                        Request.Method.PUT,
                        "/leader/eject/"+gno+"/"+item.getMno(),
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response->{
                            Toast.makeText(context,"강제 추방 성공",Toast.LENGTH_SHORT).show();
                            reload(context);
                        },
                        error->{
                            Toast.makeText(context,"강제 추방 실패",Toast.LENGTH_SHORT).show();
                        }
                );
                dialog.dismiss();
            });
            ad.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            ad.show();
        });
        return view;
    }
    private void reload(Context ctx){
        Activity activity = (Activity)ctx;
        activity.finish();

        Intent intent = new Intent(ctx, MemberManageActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("gno",gno);
        activity.startActivity(intent);
    }
}
