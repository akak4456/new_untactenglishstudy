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
import com.example.study.member.vo.MemberEntity;
import com.example.study.member.vo.MemberVO;
import com.example.study.util.JsonRequestUtil;

public class ModifyInfoActivity extends AppCompatActivity {
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_member);

        context = this;

        TextView email = findViewById(R.id.email);
        EditText name = findViewById(R.id.name);

        MemberEntity member = MyMemberInfo.getInstance().getMember();
        email.setText(member.getEmail());
        name.setText(member.getName());

        Button btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(view->{
            new JsonRequestUtil<String>(context).request(
                    Request.Method.PUT,
                    "/member/info",
                    String.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    new MemberVO(email.getText().toString(),null,name.getText().toString()),
                    response->{
                        Toast.makeText(context,"회원정보를 수정하였습니다. 다시 로그인 해주세요.",Toast.LENGTH_SHORT).show();
                        CommonHeader.getInstance().invalidate();
                        finish();

                        Intent intent = new Intent(context, LoginActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    },
                    error->{
                        Toast.makeText(context,"회원정보를 수정하지 못했습니다. 이름은 비어있으면 안됩니다.",Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
