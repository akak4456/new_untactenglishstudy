package com.example.study.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.study.R;
import com.example.study.global.Invite;
import com.example.study.member.LoginActivity;

public class IntroActivity extends Activity {

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if(getIntent().getAction().equals("android.intent.action.VIEW")){
            //초대를 통해 들어온다면
            //초대 URL은 /join/{groupid}/{inviteCode}임
            String[] urlPart = getIntent().getDataString().split("/");
            if(urlPart.length == 6) {
                //URL이 유효하다면
                Long gno = Long.parseLong(urlPart[urlPart.length - 2]);
                String inviteCode = urlPart[urlPart.length - 1];
                Invite.getInstance().setGno(gno);
                Invite.getInstance().setInviteCode(inviteCode);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(r, 2000); // 2초 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r); // 예약 취소
    }

}