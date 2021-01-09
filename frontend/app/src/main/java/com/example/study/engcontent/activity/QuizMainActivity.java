package com.example.study.engcontent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.conference.SessionActivity;

public class QuizMainActivity extends AppCompatActivity {
    private Context context;
    private Long gno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        Button btn_spell_to_mean = findViewById(R.id.btn_spell_to_mean);
        btn_spell_to_mean.setOnClickListener(view->{
            Intent intent = new Intent(context,QuizSelectVocaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("quiz_case",1);
            context.startActivity(intent);
            finish();
        });
        Button btn_mean_to_spell = findViewById(R.id.btn_mean_to_spell);
        btn_mean_to_spell.setOnClickListener(view->{
            Intent intent = new Intent(context,QuizSelectVocaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("quiz_case",2);
            context.startActivity(intent);
            finish();
        });
        Button btn_thesaurus = findViewById(R.id.btn_thesaurus);
        btn_thesaurus.setOnClickListener(view->{
            Intent intent = new Intent(context,QuizSelectDifficultyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("quiz_case",3);
            context.startActivity(intent);
            finish();
        });
        Button btn_antonym = findViewById(R.id.btn_antonym);
        btn_antonym.setOnClickListener(view->{
            Intent intent = new Intent(context,QuizSelectDifficultyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("quiz_case",4);
            context.startActivity(intent);
            finish();
        });
        Button btn_blank = findViewById(R.id.btn_blank);
        btn_blank.setOnClickListener(view->{
            Intent intent = new Intent(context,QuizSelectVocaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("quiz_case",5);
            context.startActivity(intent);
            finish();
       });

        Button btn_phrase = findViewById(R.id.btn_phrase);
        btn_phrase.setOnClickListener(view->{
            /*
            이것은 예외적으로 단어장 또는 난이도를 선택할 필요가 없도록 하였다.
            대상이 되는 구동사가 그렇게 많지 않기 때문이다.
             */
            Intent intent = new Intent(getApplicationContext(), QuizCardActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("gno",gno);
            intent.putExtra("vno",0L);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            intent.putExtra("quiz_case",6);
            intent.putExtra("difficulty","any");
            context.startActivity(intent);
            finish();
        });
    }
}
