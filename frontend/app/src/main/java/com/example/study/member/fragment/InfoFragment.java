package com.example.study.member.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.board.BoardAddActivity;
import com.example.study.global.CommonHeader;
import com.example.study.member.CheckEmailActivity;
import com.example.study.member.LoginActivity;
import com.example.study.member.ModifyInfoActivity;
import com.example.study.member.ModifyPasswordActivity;
import com.example.study.member.PayActivity;
import com.example.study.member.RefundActivity;
import com.example.study.member.vo.MemberEntity;
import com.example.study.global.MyMemberInfo;
import com.example.study.R;
import com.example.study.util.JsonRequestUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    private View view;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_point;
    private TextView tv_refund;
    private Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container,false);
        context = view.getContext();
        tv_name = (TextView) view.findViewById(R.id.my_name);
        tv_email = (TextView) view.findViewById(R.id.my_email);
        tv_point = (TextView) view.findViewById(R.id.my_point);
        tv_refund = (TextView) view.findViewById(R.id.my_refund);

        // db 연결 -> textview에 띄우기


        Button btn_chk_email = view.findViewById(R.id.btn_chk_email);
        btn_chk_email.setOnClickListener(v->{
            if(MyMemberInfo.getInstance().getMember().getEmailCheck().equals("Y")){
                Toast.makeText(context,"이미 이메일 인증이 되었습니다",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, CheckEmailActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        Button btn_modify_info = view.findViewById(R.id.btn_modify_info);
        btn_modify_info.setOnClickListener(v->{
            Intent intent = new Intent(context, ModifyInfoActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        Button btn_modify_pw = view.findViewById(R.id.btn_modify_pw);
        btn_modify_pw.setOnClickListener(v->{
            Intent intent = new Intent(context, ModifyPasswordActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        Button btn_withdraw = view.findViewById(R.id.btn_withdraw);
        btn_withdraw.setOnClickListener(v->{

            //예 아니오 다이얼 로그
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("회원 탈퇴");
            builder.setMessage("정말로 회원 탈퇴를 하시겠습니까?");

            builder.setPositiveButton("예", (dialog, which) -> {
                Activity activity = (Activity)context;
                new JsonRequestUtil<String>(context).request(
                        Request.Method.DELETE,
                        "/member/info",
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response->{
                            Toast.makeText(context,"회원 탈퇴를 완료했습니다.", Toast.LENGTH_SHORT).show();
                            activity.finish();

                            Intent intent = new Intent(context, LoginActivity.class );
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        },
                        error->{

                        }
                );
                dialog.dismiss();
                CommonHeader.getInstance().invalidate();
            });

            builder.setNegativeButton("아니오", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alert = builder.create();
            alert.show();
            //예 아니오 다이얼 로그

        });

        Button btn_pay = view.findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(v->{
            Intent intent = new Intent(context, PayActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        Button btn_refund = view.findViewById(R.id.btn_refund);
        btn_refund.setOnClickListener(v->{
            Intent intent = new Intent(context, RefundActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        new JsonRequestUtil<MemberEntity>(context).request(
                Request.Method.GET,
                "/member",
                MemberEntity.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    MemberEntity member = response;
                    tv_name.setText(member.getName());
                    tv_email.setText(member.getEmail());
                    tv_point.setText(member.getRemainPoint()+"원");
                    tv_refund.setText(member.getRefundPoint()+"원");
                },
                error->{

                }
        );
        Log.d("VIEW","START INFO");
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("VIEW","DESTROY INFO");
    }


}
