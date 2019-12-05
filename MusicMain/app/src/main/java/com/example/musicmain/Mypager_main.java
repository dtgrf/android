package com.example.musicmain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Mypager_main extends FragmentPagerAdapter {
    private ArrayList<Fragment> myViewList;
    private ArrayList<String> titlelist = new ArrayList<String>();

    public Mypager_main(@NonNull FragmentManager fm,ArrayList<Fragment> musicArrayList) {
        super(fm);
        titlelist.add("  本地音乐  ");
        titlelist.add("  网络乐库  ");
        this.myViewList=musicArrayList;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myViewList.get(position);
    }

    @Override
    public int getCount() {
        return myViewList.size();
    }


}
