package com.example.study.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.alarm.AlarmHATT;
import com.example.study.engcontent.activity.QuizCardActivity;
import com.example.study.engcontent.activity.VocaAddActivity;
import com.example.study.engcontent.activity.VocaCardActivity;
import com.example.study.engcontent.activity.VocaListActivity;
import com.example.study.engcontent.activity.VocaTableActivity;
import com.example.study.global.Invite;
import com.example.study.member.vo.LoginResultVO;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.R;
import com.example.study.main.MainActivity;
import com.example.study.member.vo.LoginVO;
import com.example.study.study.MyStudyActivity;
import com.example.study.util.JsonRequestUtil;

;

public class LoginActivity extends AppCompatActivity {
    private Context context;//HTTP REQUEST AND RESPONSE를 위해서 필요함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        // 로그인 버튼
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        Button button1 = findViewById(R.id.button_login);
        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new JsonRequestUtil<LoginResultVO>(getApplicationContext()).request(
                        Request.Method.POST,
                        "/member/login",
                        LoginResultVO.class,
                        null,
                        new LoginVO(email.getText().toString(),password.getText().toString()),
                        response -> {
                            CommonHeader.getInstance().setJWTToken(response.getToken());
                            MyMemberInfo.getInstance().setMember(response.getMember());
                            if(Invite.getInstance().getGno() == 0L) {
                                //일반적인 상황(초대에 응한것이 아님)
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }else{
                                //초대에 응했다면
                                Long gno = Invite.getInstance().getGno();
                                String inviteCode = Invite.getInstance().getInviteCode();
                                new JsonRequestUtil<String>(context).request(
                                        Request.Method.PUT,
                                        "/invite/accept/"+gno+"/"+inviteCode,
                                        String.class,
                                        CommonHeader.getInstance().getCommonHeader(),
                                        res->{
                                            Toast.makeText(context,"초대에 응했습니다. 이제 스터디에서 활동할 수 있습니다.",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, MyStudyActivity.class );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("gno",gno);
                                            context.startActivity(intent);
                                            finish();
                                        },
                                        err->{
                                            Toast.makeText(context,"유효하지 않은 접근입니다.",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                );
                            }
                        },
                        error -> Toast.makeText(context,"로그인에 실패했습니다. 아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show()
                );
            }
        });

        // 회원가입 버튼
        Button button2 = findViewById(R.id.button_sign_up);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class );
            startActivity(intent);
        });
    }



}
