package com.example.study.ranking.adapter;

import android.content.Context;
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
import com.example.study.ranking.vo.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RankGameAdapter extends PagerAdapter {

    Context context;
    List<Problem> problems;
    ArrayList<String> userAnswer;

    public RankGameAdapter(Context context, List<Problem> problems){
        this.context = context;
        this.problems = problems;
        userAnswer = new ArrayList<>();
        for(int i=0;i<problems.size();i++){
            userAnswer.add("");
        }
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        Problem problem = problems.get(position);
        if(problem.getKind().equals("blank")){
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.quiz_page_2, container, false);
            final TextView textQst = (TextView)layout.findViewById(R.id.question);
            final EditText textWord1 = (EditText) layout.findViewById(R.id.answer);
            String newProblem = "";
            Random random = new Random();
            int len = problem.getSpelling().length();
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
            StringBuilder newSpelling = new StringBuilder(problem.getSpelling());
            for (Integer idx : blankIdx) {
                newSpelling.replace(idx, idx + 1, "_");
            }
            newProblem += newSpelling.toString();
            newProblem += "\n";
            newProblem += problem.getQuestion();
            textQst.setText(newProblem);

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
            if(problem.getKind().equals("spelltomean")){
                textQst.setText(problem.getQuestion()+"에 가장 가까운 뜻은?");
            }else if(problem.getKind().equals("meantospell")){
                textQst.setText(problem.getQuestion()+"을(를) 뜻하는 단어는?");
            }else if(problem.getKind().equals("thesaurus")){
                textQst.setText(problem.getQuestion()+"의 유의어로 가장 알맞은 단어는?");
            }else if(problem.getKind().equals("antonym")){
                textQst.setText(problem.getQuestion()+"의 반의어로 가장 알맞은 단어는?");
            }else{
                textQst.setText(problem.getQuestion());
            }
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
            ansList.set(pos.get(0),problem.getAns1());
            ansList.set(pos.get(1),problem.getAns2());
            ansList.set(pos.get(2),problem.getAns3());
            ansList.set(pos.get(3),problem.getAns4());

            textWord1.setText(ansList.get(0));
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
            textWord2.setText(ansList.get(1));
            textWord2.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord2.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord2.getText().toString());
            });
            textWord3.setText(ansList.get(2));
            textWord3.setOnClickListener(v->{
                textWord1.setBackgroundResource(R.drawable.background_quiz);
                textWord2.setBackgroundResource(R.drawable.background_quiz);
                textWord3.setBackgroundResource(R.drawable.background_quiz);
                textWord4.setBackgroundResource(R.drawable.background_quiz);

                textWord3.setBackgroundResource(R.drawable.background_quiz_pressed);

                userAnswer.set(position,textWord3.getText().toString());
            });
            textWord4.setText(ansList.get(3));
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
        return problems.size();
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
