package com.example.study.member;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.util.JsonRequestUtil;

public class CheckEmailActivity extends AppCompatActivity {
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);
        context = this;
        EditText check_code = findViewById(R.id.check_code);

        Button btn_check_code = findViewById(R.id.btn_check_code);

        btn_check_code.setOnClickListener(view->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.POST,
                    "/member/emailcheck/"+check_code.getText().toString(),
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response->{
                        MyMemberInfo.getInstance().getMember().setEmailCheck("Y");
                        Toast.makeText(CheckEmailActivity.this, "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Activity activity = (Activity)context;
                        activity.finish();
                    },
                    error->{
                        Toast.makeText(CheckEmailActivity.this, "인증코드가 맞지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
            );
        });

        Button btn_send_again = findViewById(R.id.btn_send_again);
        btn_send_again.setOnClickListener(v->{
            sendCheckCodeToEmail();
        });
        sendCheckCodeToEmail();
    }
    private void sendCheckCodeToEmail(){
        new JsonRequestUtil<String>(context).request(
                Request.Method.POST,
                "/member/sendcheckcode",
                String.class,
                CommonHeader.getInstance().getCommonHeader(),
                response->{

                },
                error->{

                }
        );
    }
}
