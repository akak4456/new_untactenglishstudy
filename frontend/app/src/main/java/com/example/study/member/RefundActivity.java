package com.example.study.member;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.member.vo.MemberEntity;
import com.example.study.util.JsonRequestUtil;

public class RefundActivity extends AppCompatActivity {
    private Context context;
    private Long remainAllPoint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        context = this;
        TextView tv_cur_point = findViewById(R.id.tv_cur_point);

        new JsonRequestUtil<MemberEntity>(context).request(
                Request.Method.GET,
                "/member",
                MemberEntity.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{
                    MemberEntity member = response;
                    tv_cur_point.setText("남은 포인트:"+member.getRemainPoint()+"원");
                    remainAllPoint = member.getRemainPoint();
                },
                error->{

                }
        );
        EditText amount = findViewById(R.id.amount);

        Button btn_50000 = findViewById(R.id.btn_50000);
        btn_50000.setOnClickListener(view->{
            Long a = 0L;
            String aStr = amount.getText().toString();
            if(aStr != null && !aStr.equals("")){
                a = Long.parseLong(amount.getText().toString());
            }
            amount.setText(String.valueOf(a+50000L));
        });
        Button btn_30000 = findViewById(R.id.btn_30000);
        btn_30000.setOnClickListener(view->{
            Long a = 0L;
            String aStr = amount.getText().toString();
            if(aStr != null && !aStr.equals("")){
                a = Long.parseLong(amount.getText().toString());
            }
            amount.setText(String.valueOf(a+30000L));
        });

        Button btn_10000 = findViewById(R.id.btn_10000);
        btn_10000.setOnClickListener(view->{
            Long a = 0L;
            String aStr = amount.getText().toString();
            if(aStr != null && !aStr.equals("")){
                a = Long.parseLong(amount.getText().toString());
            }
            amount.setText(String.valueOf(a+10000L));
        });

        Button btn_refund = findViewById(R.id.btn_refund);
        btn_refund.setOnClickListener(view->{
            String aStr = amount.getText().toString();
            if(aStr != null && !aStr.equals("")) {
                new JsonRequestUtil<String>(context).request(
                        Request.Method.PUT,
                        "/member/refund/" + amount.getText().toString(),
                        String.class,
                        CommonHeader.getInstance().getCommonHeader(),
                        response -> {
                            Toast.makeText(context, "환급을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        },
                        error -> {
                            Toast.makeText(context, "환급을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

        Button btn_refund_all = findViewById(R.id.btn_refund_all);
        btn_refund_all.setOnClickListener(view->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/member/refund/" + String.valueOf(remainAllPoint),
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        Toast.makeText(context, "환급을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    },
                    error -> {
                        Toast.makeText(context, "환급을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
