package com.example.study.main.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.study.study.fragment.FindFragment;
import com.example.study.member.fragment.InfoFragment;
import com.example.study.study.fragment.StudyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter_Main extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();

    public SectionsPagerAdapter_Main(FragmentManager fm) {
        super(fm);
        fragmentList.add(new StudyFragment());
        fragmentTitleList.add("내 스터디");

        fragmentList.add(new FindFragment());
        fragmentTitleList.add("검색");

        fragmentList.add(new InfoFragment());
        fragmentTitleList.add("설정");
    }

    @Override
    public Fragment getItem(int position) {
       return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return fragmentList.size();
    }
}