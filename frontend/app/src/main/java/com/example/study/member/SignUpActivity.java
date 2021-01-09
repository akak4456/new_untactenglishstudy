package com.example.study.member;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.member.vo.EmailCheckVO;
import com.example.study.member.vo.MemberVO;
import com.example.study.util.JsonRequestUtil;

import java.io.UnsupportedEncodingException;

public class SignUpActivity extends AppCompatActivity {
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;

        EditText Sign_up_email = findViewById(R.id.Sign_up_email);
        EditText Sign_up_name = findViewById(R.id.Sign_up_name);
        EditText Sign_up_pwd = findViewById(R.id.Sign_up_pwd);
        EditText Sign_up_pwd_chk = findViewById(R.id.Sign_up_pwd_chk);

        Button button_sign_up_form = findViewById(R.id.button_sign_up_form);
        button_sign_up_form.setOnClickListener(view -> {
            String email = Sign_up_email.getText().toString();
            String name = Sign_up_name.getText().toString();
            String pw = Sign_up_pwd.getText().toString();
            String pw_chk = Sign_up_pwd_chk.getText().toString();
            //이메일이 맞는지, 비밀번호의 길이 및 형식은 맞는지, 이름은 비어있지 않는지도 테스트 해보아야 함
            if(!pw.equals(pw_chk)){
                Toast.makeText(SignUpActivity.this, "비밀번호와 비밀번호 확인이 같지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            new JsonRequestUtil<String>(context).request(
                    Request.Method.POST,
                    "/member/sign_up",
                    String.class,
                    null,
                    new MemberVO(email,pw,name),
                    response->{
                        Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다. 나중에 이메일 인증을 해주시길 바랍니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error->{
                        try {
                            String body = new String(error.networkResponse.data,"UTF-8");
                            if(body.equals("emailnotnull")){
                                Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if(body.equals("emailregexp")){
                                Toast.makeText(SignUpActivity.this, "이메일 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                            }else if(body.equals("pwnotnull")){
                                Toast.makeText(SignUpActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if(body.equals("pwregexp")){
                                Toast.makeText(SignUpActivity.this, "비밀번호는 최소 8자 최대 50자입니다. 그리고 영어 대소문자, 숫자, 특수문자 모두를 포함해야 합니다.", Toast.LENGTH_SHORT).show();
                            }else if(body.equals("namenotnull")){
                                Toast.makeText(SignUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if(body.equals("emailduplicate")){
                                Toast.makeText(SignUpActivity.this, "이메일이 중복됩니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
            );
        });
        Button button_check = findViewById(R.id.button_check);
        button_check.setOnClickListener(view->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.POST,
                    "/member/emailcheck",
                    String.class,
                    null,
                    new EmailCheckVO(Sign_up_email.getText().toString()),
                    response->{
                        Toast.makeText(SignUpActivity.this, "그 이메일을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    },
                    error->{
                        Toast.makeText(SignUpActivity.this, "중복되는 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
            );
        });

    }
}
