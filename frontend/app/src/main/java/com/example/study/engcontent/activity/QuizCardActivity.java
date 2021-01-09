package com.example.study.engcontent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.study.R;
import com.example.study.board.vo.BoardItemVO;
import com.example.study.engcontent.adapter.QuizPagerAdapter;
import com.example.study.engcontent.adapter.VocaPagerAdapter;
import com.example.study.engcontent.vo.QuizResponse;
import com.example.study.engcontent.vo.QuizVO;
import com.example.study.global.CommonHeader;
import com.example.study.util.JsonRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizCardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private QuizPagerAdapter pagerAdapter;
    private int quiz_case;
    private String difficulty;
    ArrayList<String> question;
    ArrayList<String> choice1;
    ArrayList<String> choice2;
    ArrayList<String> choice3;
    ArrayList<String> choice4;
    ArrayList<String> actualAnswer;
    private Long gno;
    private Long vno;
    private int cnt=20;//문제 수
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_card);

        question = new ArrayList<>();
        choice1 = new ArrayList<>();
        choice2 = new ArrayList<>();
        choice3 = new ArrayList<>();
        choice4 = new ArrayList<>();
        actualAnswer = new ArrayList<>();
        // 현재는 임의로 추가
        // 서버 연동 필요

        context = this;
        gno = getIntent().getLongExtra("gno",0L);
        vno = getIntent().getLongExtra("vno",0L);
        quiz_case = getIntent().getIntExtra("quiz_case",0);
        difficulty = getIntent().getStringExtra("difficulty");
        if(quiz_case == 1){
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/spelltomean/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        makeQuizFromResponse(response);
                    },
                    error -> {
                        Toast.makeText(context,"퀴즈를 만들기에 단어가 너무 적습니다. 최소 100개 이상의 단어를 넣어주세요",Toast.LENGTH_SHORT).show();
                    }
            );
        }
        else if(quiz_case == 2){
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/meantospell/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        makeQuizFromResponse(response);
                    },
                    error -> {
                        Toast.makeText(context,"퀴즈를 만들기에 단어가 너무 적습니다. 최소 100개 이상의 단어를 넣어주세요",Toast.LENGTH_SHORT).show();
                    }
            );
        }
        else if(quiz_case == 3){
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/thesaurus/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        makeQuizFromResponse(response);
                    },
                    error -> {

                    }
            );
        }
        else if(quiz_case == 4){
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/antonym/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        makeQuizFromResponse(response);
                    },
                    error -> {

                    }
            );
        }
        else if(quiz_case == 5) {
            //유형5
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/blank/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        String json = new Gson().toJson(response.getQuiz());
                        Type listType = new TypeToken<List<QuizVO>>() {
                        }.getType();
                        List<QuizVO> list = new Gson().fromJson(json, listType);
                        for (QuizVO vo : list) {
                            String newProblem = "";
                            Random random = new Random();
                            int len = vo.getSpelling().length();
                            Set<Integer> blankIdx = new HashSet<>();
                            if (len <= 5) {
                                while (blankIdx.size() < 2) {
                                    blankIdx.add(random.nextInt(len - 1) + 1);
                                }
                            } else if (6 <= len && len <= 7) {
                                while (blankIdx.size() < 3) {
                                    blankIdx.add(random.nextInt(len));
                                }
                            } else if (8 <= len && len <= 10) {
                                while (blankIdx.size() < 4) {
                                    blankIdx.add(random.nextInt(len));
                                }
                            } else {
                                while (blankIdx.size() < 5) {
                                    blankIdx.add(random.nextInt(len - 5));
                                }
                            }
                            StringBuilder newSpelling = new StringBuilder(vo.getSpelling());
                            for (Integer idx : blankIdx) {
                                newSpelling.replace(idx, idx + 1, "_");
                            }
                            newProblem += newSpelling.toString();
                            newProblem += "\n";
                            newProblem += vo.getProblem();
                            question.add(newProblem);
                            actualAnswer.add(vo.getSpelling());
                        }
                        pagerAdapter = new QuizPagerAdapter(this, quiz_case, question);
                        viewPager.setAdapter(pagerAdapter);
                    },
                    error -> {
                        Toast.makeText(context,"퀴즈를 만들기에 단어가 너무 적습니다. 최소 100개 이상의 단어를 넣어주세요",Toast.LENGTH_SHORT).show();
                    }
            );
        }
        else if(quiz_case == 6){
            new JsonRequestUtil<QuizResponse>(context).request(
                    Request.Method.POST,
                    "/quiz/" + gno + "/" + vno + "/phrase/" + cnt+"/"+difficulty,
                    QuizResponse.class,
                    CommonHeader.getInstance().getCommonHeader(),
                    response -> {
                        makeQuizFromResponse(response);
                    },
                    error -> {

                    }
            );
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager_quiz);
        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), QuizReusltActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("question",question);
            intent.putExtra("user_answer",pagerAdapter.getUserAnswer());
            intent.putExtra("actual_answer",actualAnswer);//기본 단어장을 사용해 퀴즈를 만들면 0L을 보내야 함
            context.startActivity(intent);
            finish();
        });

    }

    private void makeQuizFromResponse(QuizResponse response){
        String json = new Gson().toJson(response.getQuiz());
        Type listType = new TypeToken<List<QuizVO>>() {
        }.getType();
        List<QuizVO> list = new Gson().fromJson(json, listType);
        for (QuizVO vo : list) {
            question.add(vo.getProblem());
            actualAnswer.add(vo.getAns1());//항상 Ans1 컬럼이 정답임
            List<Integer> pos = new ArrayList<>();
            pos.add(0);
            pos.add(1);
            pos.add(2);
            pos.add(3);
            Collections.shuffle(pos);
            List<String> ansList = new ArrayList<>();
            ansList.add("");
            ansList.add("");
            ansList.add("");
            ansList.add("");
            ansList.set(pos.get(0),vo.getAns1());
            ansList.set(pos.get(1),vo.getAns2());
            ansList.set(pos.get(2),vo.getAns3());
            ansList.set(pos.get(3),vo.getAns4());
            choice1.add(ansList.get(0));
            choice2.add(ansList.get(1));
            choice3.add(ansList.get(2));
            choice4.add(ansList.get(3));
        }
        pagerAdapter = new QuizPagerAdapter(this, quiz_case, question,choice1,choice2,choice3,choice4);
        viewPager.setOffscreenPageLimit(question.size());
        viewPager.setAdapter(pagerAdapter);
    }
}