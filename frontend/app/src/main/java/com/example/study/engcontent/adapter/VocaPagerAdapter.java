package com.example.study.engcontent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.study.R;

import java.util.ArrayList;

public class VocaPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> word;
    ArrayList<String> mean;
    TextView textMean;




    public VocaPagerAdapter(Context context, ArrayList<String> word, ArrayList<String> mean){
        this.context = context;
        this.word = word;
        this.mean = mean;
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {


        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.voca_card_page, container, false);
        final TextView textWord = (TextView)layout.findViewById(R.id.word);
        textMean = (TextView)layout.findViewById(R.id.mean);
        textWord.setText(word.get(position));
        textMean.setText(mean.get(position));



        // 뷰페이저에 추가.
        container.addView(layout,0);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return word.size();
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





}
