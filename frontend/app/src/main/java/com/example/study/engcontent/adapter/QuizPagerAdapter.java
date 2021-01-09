package com.example.study.engcontent.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.study.R;


import java.util.ArrayList;

public class QuizPagerAdapter extends PagerAdapter {

    Context context;
    int quiz_case;
    ArrayList<String> question;
    ArrayList<String> choice1;
    ArrayList<String> choice2;
    ArrayList<String> choice3;
    ArrayList<String> choice4;
    ArrayList<String> userAnswer;


    //유형5번(blank quiz)
    public QuizPagerAdapter(Context context,int quiz_case, ArrayList<String> question){
        this.context = context;
        this.question = question;
        this.quiz_case = quiz_case;
        this.userAnswer = new ArrayList<>();
        for(int i=0;i<question.size();i++){
            userAnswer.add("");
        }
    }

    //나머지 4가지 유형
    public QuizPagerAdapter(Context context,int quiz_case, ArrayList<String> question, ArrayList<String> choice1,
                            ArrayList<String> choice2,ArrayList<String> choice3, ArrayList<String> choice4){
        this.context = context;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.quiz_case = quiz_case;
        this.userAnswer = new ArrayList<>();
        for(int i=0;i<question.size();i++){
            userAnswer.add("");
        }
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        if(quiz_case==5){
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.quiz_page_2, container, false);
            final TextView textQst = (TextView)layout.findViewById(R.id.question);
            final EditText textWord1 = (EditText) layout.findViewById(R.id.answer);

            textQst.setText(question.get(position));

            textWord1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    userAnswer.set(position,textWord1.getText().toString());
                }
            });
            // 뷰페이저에 추가.
            container.addView(layout,0);

            return layout;
        }
        else{
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.quiz_page_1, container, false);
            final TextView textQst = (TextView)layout.findViewById(R.id.question);
            final TextView textWord1 = (TextView)layout.findViewById(R.id.word1);
            final TextView textWord2 = (TextView)layout.findViewById(R.id.word2);
            final TextView textWord3 = (TextView)layout.findViewById(R.id.word3);
            final TextView textWord4 = (TextView)layout.findViewById(R.id.word4);

            textQst.setText(question.get(position));
            textWord1.setText(choice1.get(position));
            if(userAnswer.get(position).equals(textWord1.getText())){
                textWord1.setBackgroundResource(R.drawable.background_quiz_pressed);
            }
            if(userAnswer.get(position).equals(textWord2.getText())){
                textWord2.setBackgroundResource(R.drawable.background_quiz_pressed);
            }
            if(userAnswer.get(position).equals(textWord3.getText())){
                textWord3.setBackgroundResource(R.drawable.background_quiz_pressed);
            }
            if(userAnswer.get(position).equals(textWord4.getText())){
                textWord4.setBackgroundResource(R.drawable.background_quiz_pressed);
            }
            textWord1.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord1.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord1.getText().toString());
            });
            textWord2.setText(choice2.get(position));
            textWord2.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord2.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord2.getText().toString());
            });
            textWord3.setText(choice3.get(position));
            textWord3.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord3.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord3.getText().toString());
            });
            textWord4.setText(choice4.get(position));
            textWord4.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord4.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord4.getText().toString());
            });

            // 뷰페이저에 추가.
            container.addView(layout,0);

            return layout;
        }


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return question.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (View)object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public ArrayList<String> getUserAnswer(){
        return this.userAnswer;
    }
}
