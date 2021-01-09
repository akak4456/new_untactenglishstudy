package com.example.study.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.study.R;
import com.example.study.main.adapter.SectionsPagerAdapter_Main;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.view_pager);
        SectionsPagerAdapter_Main sectionsPagerAdapterMain = new SectionsPagerAdapter_Main(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapterMain);
        viewPager.setOffscreenPageLimit(sectionsPagerAdapterMain.getCount());
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}