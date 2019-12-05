package com.example.musicmain;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class Musicservice extends Service {
    public MediaPlayer mediaPlayer;
         public  class Mybinder extends Binder{ public void change(String path){Musicservice.this.change(path);}
         public void plays(String path){
             play(path);
         }
             public void plays(){
                 play();
             }
         public int getcurrentposition(){
            return getCurrentprogress();
         }
         public boolean isplay(){
           return Musicservice.this.isplay();
         }
         public int getMusicWidth(){
            return getLength();
         }
         }
    private Mybinder mybinder;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public boolean isplay(){
         if(mediaPlayer!=null&& mediaPlayer.isPlaying())
             return true;
         else
             return false;

    }

    public void play(String path)  {
         if(mediaPlayer==null)
         {
             mediaPlayer=new MediaPlayer();
             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
             try {

                 mediaPlayer.setDataSource(path);
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                // mediaPlayer.reset();//从error状态恢复
                 mediaPlayer.prepare();
             } catch (IOException e) {
                 Toast.makeText(this,"请重试",Toast.LENGTH_LONG).show();
                 e.printStackTrace();
             }
             mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听准备情况
                 @Override
                 public void onPrepared(MediaPlayer mediaPlayer) {
                     mediaPlayer.start();
                 }
             });
         }
         else if(mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
              int position= mediaPlayer.getCurrentPosition();
              mediaPlayer.seekTo(position);
            /* try {
                 mediaPlayer.prepare();
             } catch (IOException e) {
                 e.printStackTrace();
             }*/
             //mediaPlayer.reset();
              mediaPlayer.start();
         }
         else if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
             mediaPlayer.pause();

         }
    }
    public void play()  {
         if(mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            int position= mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(position);
            /* try {
                 mediaPlayer.prepare();
             } catch (IOException e) {
                 e.printStackTrace();
             }*/
            //mediaPlayer.reset();
            mediaPlayer.start();
        }
        else if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();

        }
    }
    public void change(String path){

            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
            try {
                mediaPlayer.setDataSource(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // mediaPlayer.reset();//从error状态恢复
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听准备情况
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

    }
  /*  public void pause(){
         if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
             mediaPlayer.pause();
         }
         else if(mediaPlayer!=null&&(!mediaPlayer.isPlaying())){
             mediaPlayer.start();
         }
    }
*/
   /* public void stop(){
         if(mediaPlayer!=null) {
             mediaPlayer.stop();
             mediaPlayer.release();
             mediaPlayer=null;
         }
    }*/
    public int getLength(){
         if(mediaPlayer!=null)
         {
             return mediaPlayer.getDuration();
         }
         else return 0;
    }
    public int getCurrentprogress(){
         if (mediaPlayer!=null){
             if(mediaPlayer.isPlaying())
             {return mediaPlayer.getCurrentPosition();}
             else if(!mediaPlayer.isPlaying()){
                 return mediaPlayer.getCurrentPosition();
             }
             else return 0;
         }
         else
          return 0;
    }
    public void onDestory(){
         if(mediaPlayer!=null) {
             mediaPlayer.stop();
             mediaPlayer.release();
             mediaPlayer=null;
         }
         super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }
}
