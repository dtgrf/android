package com.example.musicmain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MypagerAdapter_musiclist extends FragmentPagerAdapter {
  private ArrayList<Fragment> myViewList;
  private ArrayList<String> titlelist = new ArrayList<String>();

  public MypagerAdapter_musiclist(@NonNull FragmentManager fm, ArrayList<Fragment> myViewList) {
    super(fm);
    titlelist.add("歌曲     ");
    titlelist.add("歌手     ");
    titlelist.add("专辑 ");
    this.myViewList = myViewList;

  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return titlelist.get(position);
  }

  @Override
  public int getCount() {
    return myViewList.size();
  }


  @NonNull
  @Override
  public Fragment getItem(int position) {
    return myViewList.get(position);
  }
}