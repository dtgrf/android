package com.example.musicmain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.musicmain.local_select;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.musicmain.local_select;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.Toast;

//import com.example.musicmain.local_select.OnFragmentInteractionListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.musicmain.local_detail.*;

public class guide extends AppCompatActivity {

    // public TabHost tabHost;
    TabLayout tabLayout;
    static boolean servicestatus=false;
    ViewPager viewPager;
    static Thread thread;
    ArrayList<Fragment> myfragmentlist;
    Mypager_main mypager;
    SeekBar seekBar;
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getpresssion();
        setContentView(R.layout.activity_guide);
        tabLayout=findViewById(R.id.mytab);
        viewPager=findViewById(R.id.viewpage);
        //li=getLayoutInflater();
        startorstop=findViewById(R.id.playorstop);
        seekBar=findViewById(R.id.seekbar);
        checkupdata();
        musiclists=new ArrayList<>();
        initmusiclist();
        hander=new myhandler();
        local_detail.seekBar=this.findViewById(R.id.seekbar);
        setViewpager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(mypager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        startservice();
        startorstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random=new Random();
                local_detail.path= musiclists.get(random.nextInt(musiclists.size())).path;
                if(path!=null){
                    if (!mybinder.isplay())
                        startorstop.setBackgroundResource(R.mipmap.pause);
                    else if (mybinder.isplay())
                        startorstop.setBackgroundResource(R.mipmap.play);
                    if (!TextUtils.isEmpty(local_detail.path)) {//判断字符非空
                        mybinder.plays(local_detail.path);
                        initseekbar(seekBar);
                        updateprogress(seekBar);

                    } else {
                        Toast.makeText(guide.this, "找不到文件", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(guide.this,"无音乐",Toast.LENGTH_LONG).show();

                }
            }

        });
        buttonstatus();
    }

    static void buttonstatus(){
        if (!mybinder.isplay())
            startorstop.setBackgroundResource(R.mipmap.play);
        else if (mybinder.isplay())
            startorstop.setBackgroundResource(R.mipmap.pause);

    }
    private void startservice(){
        if(!servicestatus) {
            Musicservice musicservice = new Musicservice();
            mybinder = musicservice.new Mybinder();
            Intent intent = new Intent(guide.this, Musicservice.class);
            startService(intent);
            servicestatus = true;
        }
    }


    private void checkupdata(){
        if(thread!=null){
            thread.interrupt();
            initseekbar(seekBar);
            updateprogress(seekBar);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void initmusiclist(){
        MyMusicSqlite myMusicSqlite=new MyMusicSqlite(guide.this,"mymusicdb",null,1);
        ContentResolver contentResolver=guide.this.getContentResolver();
        Cursor cursor= contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null);
        String stit=null;
        String ssing=null;
        String spath=null;
        int sid=0;
        String sablum=null;
        if(myMusicSqlite.exequery()==0) {
            while (cursor.moveToNext()) {
                //sid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                stit = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                ssing = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                spath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                sablum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                Music music=new Music(stit,ssing,spath,sablum);
                myMusicSqlite.add(music);
            }

        }
        else {
            musiclists=myMusicSqlite.getlist();
        }
        myMusicSqlite.close();
        musiclists=myMusicSqlite.getlist();

    }
    private void setFragment(){
        myfragmentlist=new ArrayList<>();
        Fragment fragment=local_select.newInstance(null,null);
        Fragment fragment1=webmusic.newInstance(null,null);
        myfragmentlist.add(fragment);
        myfragmentlist.add(fragment1);
    }
    private void setViewpager(){
        setFragment();
        mypager=new Mypager_main(getSupportFragmentManager(),myfragmentlist);
        viewPager.setAdapter(mypager);
        viewPager.setOffscreenPageLimit(2);//个数限制
        viewPager.setCurrentItem(0);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void getpresssion(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(guide.this,"获权成功",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(guide.this,"获权失败",Toast.LENGTH_LONG).show();

            }

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
