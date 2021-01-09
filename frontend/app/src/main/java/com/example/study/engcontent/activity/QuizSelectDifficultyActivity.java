package com.example.study.engcontent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;

public class QuizSelectDifficultyActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    private int quiz_case;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select_difficulty);
        gno = getIntent().getLongExtra("gno",0L);
        quiz_case = getIntent().getIntExtra("quiz_case",0);
        context = this;
        Button btn_soeasy = findViewById(R.id.btn_soeasy);
        btn_soeasy.setOnClickListener(view->{
            Intent intent = new Intent(context, QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",quiz_case);
            intent.putExtra("difficulty","soeasy");
            context.startActivity(intent);
            ((Activity)context).finish();
        });

        Button btn_easy = findViewById(R.id.btn_easy);
        btn_easy.setOnClickListener(view->{
            Intent intent = new Intent(context, QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",quiz_case);
            intent.putExtra("difficulty","easy");
            context.startActivity(intent);
            ((Activity)context).finish();
        });

        Button btn_normal = findViewById(R.id.btn_normal);
        btn_normal.setOnClickListener(view->{
            Intent intent = new Intent(context, QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",quiz_case);
            intent.putExtra("difficulty","normal");
            context.startActivity(intent);
            ((Activity)context).finish();
        });

        Button btn_hard = findViewById(R.id.btn_hard);
        btn_hard.setOnClickListener(view->{
            Intent intent = new Intent(context, QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",quiz_case);
            intent.putExtra("difficulty","hard");
            context.startActivity(intent);
            ((Activity)context).finish();
        });

        Button btn_sohard = findViewById(R.id.btn_sohard);
        btn_sohard.setOnClickListener(view->{
            Intent intent = new Intent(context, QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",quiz_case);
            intent.putExtra("difficulty","sohard");
            context.startActivity(intent);
            ((Activity)context).finish();
        });
    }
}
