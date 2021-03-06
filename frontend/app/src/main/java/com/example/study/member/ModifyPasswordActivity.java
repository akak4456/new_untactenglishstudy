package com.example.study.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.global.CommonHeader;
import com.example.study.global.MyMemberInfo;
import com.example.study.member.vo.ChangePwVO;
import com.example.study.member.vo.MemberEntity;
import com.example.study.member.vo.MemberVO;
import com.example.study.util.JsonRequestUtil;

public class ModifyPasswordActivity extends AppCompatActivity {
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_member_password);

        context = this;

        TextView email = findViewById(R.id.email);
        EditText cur_pw = findViewById(R.id.cur_pw);
        EditText new_pw = findViewById(R.id.new_pw);
        EditText new_pw_chk = findViewById(R.id.new_pw_chk);

        MemberEntity member = MyMemberInfo.getInstance().getMember();
        email.setText(member.getEmail());

        Button btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(view->{
            String old_pw = cur_pw.getText().toString();
            String new_pw_str = new_pw.getText().toString();
            String new_pw_chk_str = new_pw_chk.getText().toString();
            if(!new_pw_str.equals(new_pw_chk_str)){
                Toast.makeText(context,"새 비밀번호와 새 비밀번호 확인이 같지 않습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            if(old_pw.equals(new_pw_chk_str)){
                Toast.makeText(context,"현재 비밀번호와 새 비밀번호가 같습니다. 다른 비밀번호를 시도해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/member/info/pw",
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new ChangePwVO(email.getText().toString(),old_pw,new_pw_str),
                    response->{
                        Toast.makeText(context,"회원정보를 수정하였습니다. 다시 로그인 해주세요.",Toast.LENGTH_SHORT).show();
                        CommonHeader.getInstance().invalidate();
                        finish();

                        Intent intent = new Intent(context, LoginActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    },
                    error->{
                        Toast.makeText(context,"회원정보를 수정하지 못했습니다. 현재 비밀번호를 잘못 입력했거나 비밀번호 형식을 지키지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
