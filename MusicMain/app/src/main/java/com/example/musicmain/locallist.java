package com.example.musicmain;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Random;

public class locallist extends AppCompatActivity {

    static ArrayList<Music> musiclists;
    ViewPager viewPager;
    TabLayout tabLayout;
    static Button startorstop;
    Button next;
    static SeekBar seekBar;
    static local_detail.myhandler hander;
    static String path;
    static Musicservice.Mybinder mybinder;
    ImageButton retun;
    ArrayList<Fragment> myfragmentlist;
    MypagerAdapter_musiclist mypager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_detail);
        setViewpager();
        startorstop=findViewById(R.id.playorstop);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(mypager);
        seekBar=findViewById(R.id.seekbar);
        checkupdata();
        guide.buttonstatus();
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
        backmain();
        next=findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        startorstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!guide.servicestatus)
                {
                    Intent intent=new Intent(locallist.this,Musicservice.class);
                    //bindService(intent,mycon,BIND_AUTO_CREATE);//绑定方式跳转界面会停止
                    startService(intent);
                    guide.servicestatus=true;
                }
                if (!mybinder.isplay())
                {startorstop.setBackgroundResource(R.mipmap.pause);
                    if (!TextUtils.isEmpty(local_detail.path)) {//判断字符非空
                        mybinder.plays(local_detail.path);
                        initseekbar(seekBar);
                        updateprogress(seekBar);
                    }
                    else {
                        local_detail.path=musiclists.get(new Random().nextInt(musiclists.size())).path;
                        mybinder.plays(local_detail.path);
                        initseekbar(seekBar);
                        updateprogress(seekBar);
                    }

                }
                else if (mybinder.isplay())
                {startorstop.setBackgroundResource(R.mipmap.play);
                    mybinder.plays();
                }

            }
        });

    }
    private void backmain(){
        retun=findViewById(R.id.retun);
        retun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(locallist.this,guide.class);
                startActivity(intent);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Music> getmusic(){
        ContentResolver contentResolver=locallist.this.getContentResolver();
        Cursor cursor= contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null);
        String stit=null;
        String ssing=null;
        String spath=null;
        int sid=0;
        String sablum=null;
        while (cursor.moveToNext()){
            stit=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            ssing=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            spath=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            sid=Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            sablum=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            Music music=new Music(sid,stit,ssing,spath,sablum);
            local_detail.musiclists.add(music);
        }
        if(local_detail.musiclists.size()>1){
            path = local_detail.musiclists.get(new Random().nextInt(local_detail.musiclists.size())).path;}
        return local_detail.musiclists;
    }
    private void initcontrol(){
        viewPager=findViewById(R.id.detail_viewpage);
        tabLayout=findViewById(R.id.detail_mytab);
        myfragmentlist=new ArrayList<>();
        Fragment fragment=musiclist.newInstance(null,null);
        Fragment fragment1=music_byalbum.newInstance(null,null);
        Fragment fragment2=music_bysinger.newInstance(null,null);
        myfragmentlist.add(fragment);
        myfragmentlist.add(fragment2);
        myfragmentlist.add(fragment1);
    }
    private void setViewpager(){
        initcontrol();
        mypager=new MypagerAdapter_musiclist(getSupportFragmentManager(),myfragmentlist);
        viewPager.setAdapter(mypager);
        viewPager.setOffscreenPageLimit(2);//个数限制
        viewPager.setCurrentItem(0);
    }


    static void initseekbar(SeekBar seekbar){
        int musicwidth=mybinder.getMusicWidth();
        seekbar.setMax(musicwidth);
    }
    private void checkupdata(){
        if(guide.thread!=null) {
            guide.thread.interrupt();
            initseekbar(seekBar);
            updateprogress(seekBar);
        }
    }

    public static class myhandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1)
            {
                int currentposition=(Integer)msg.arg1;
                SeekBar seekBars=(SeekBar) msg.obj;
                seekBars.setProgress(currentposition);
            }
            super.handleMessage(msg);
        }
    }
    static  void updateprogress(final SeekBar seekBar){
        guide.thread = new Thread() {
            @Override
            public void run() {
                while (!interrupted())//判断线程中断状态
                {
                    int currentposition = mybinder.getcurrentposition();
                    Message message = Message.obtain();//创建对象，从缓存池返回一个实例，避免重复；
                    message.obj = seekBar;
                    message.arg1=currentposition;
                    message.what=1;
                    hander.sendMessage(message);
                }
            }
        };
        guide.thread.start();
    }

    static class Mycon implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mybinder=(Musicservice.Mybinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    //确保退出不会销毁activity
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
