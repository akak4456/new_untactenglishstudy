package com.example.study.ranking.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.global.MyMemberInfo;

import org.w3c.dom.Text;

public class RankingResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_result);
        String first_member = getIntent().getStringExtra("first_member");
        String second_member = getIntent().getStringExtra("second_member");
        String third_member = getIntent().getStringExtra("third_member");
        Long first_prize = getIntent().getLongExtra("first_prize",0L);
        Long second_prize = getIntent().getLongExtra("second_prize",0L);
        Long third_prize = getIntent().getLongExtra("third_prize",0L);

        TextView content = findViewById(R.id.content);
        String myName = MyMemberInfo.getInstance().getMember().getName();
        if(first_member.equals(myName)){
            content.setText("축하합니다. 당신은 1등입니다.");
        }else if(second_member.equals(myName)){
            content.setText("축하합니다. 당신은 2등입니다.");
        }else if(third_member.equals(myName)){
            content.setText("축하합니다. 당신은 3등입니다.");
        }else{
            content.setText("아쉽습니다. 당신은 순위에 들지 못했습니다.");
        }
        TextView header1 = findViewById(R.id.header1);
        header1.setText("1등(상금:"+first_prize+")");
        TextView content1 = findViewById(R.id.content1);
        content1.setText(first_member);

        TextView header2 = findViewById(R.id.header2);
        header2.setText("2등(상금:"+second_prize+")");
        TextView content2 = findViewById(R.id.content2);
        content2.setText(second_member);

        TextView header3 = findViewById(R.id.header3);
        header3.setText("3등(상금:"+third_prize+")");
        TextView content3 = findViewById(R.id.content3);
        content3.setText(third_member);
    }
}
