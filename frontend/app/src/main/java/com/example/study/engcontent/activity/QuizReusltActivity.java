package com.example.study.engcontent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.study.R;
import com.example.study.member.SignUpActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizReusltActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_reuslt);
        context = this;
        TableLayout result_table = (TableLayout)findViewById(R.id.result_table);
        ArrayList<String> question = (ArrayList<String>)getIntent().getSerializableExtra("question");
        ArrayList<String> userAnswer = (ArrayList<String>)getIntent().getSerializableExtra("user_answer");
        ArrayList<String> actualAnswer = (ArrayList<String>)getIntent().getSerializableExtra("actual_answer");
        for(int i=0;i<question.size();i++){

            TableRow tableRow = new TableRow(this);

            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            TextView tv_num = new TextView(this); //문제번호
            TextView tv_question = new TextView(this); //문제
            TextView tv_answer = new TextView(this); //정답
            TextView tv_ox = new TextView(this); //정답여부 o나 x로 표시
            tv_num.setText((i+1)+"");
            tv_question.setText(question.get(i));
            tv_answer.setText(actualAnswer.get(i));
            if(userAnswer.get(i).equals(actualAnswer.get(i))){
                tv_ox.setText("O");
            }else {
                tv_ox.setText("X");
            }
            final float scale = context.getResources().getDisplayMetrics().density;
            tableRow.addView(tv_num,new TableRow.LayoutParams((int)(50*scale), TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(tv_question,new TableRow.LayoutParams((int)(120*scale), TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(tv_answer,new TableRow.LayoutParams((int)(120*scale), TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(tv_ox,new TableRow.LayoutParams((int)(100*scale), TableRow.LayoutParams.WRAP_CONTENT));
            result_table.addView(tableRow);
        }

        Button button = findViewById(R.id.btn_main);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), QuizMainActivity.class );
            startActivity(intent);
            finish();
        });


    }
}