package com.example.musicmain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyMusicSqlite extends SQLiteOpenHelper {

public SQLiteDatabase dbw;
public SQLiteDatabase dbr;
    public MyMusicSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table xjmusiclocal  (id integer primary key autoincrement,"+"title varchar(20),"+"singer varchar(20),"+"path varchar (100),"+"ablum varchar (30),"+"collectby varchar(100))");
        sqLiteDatabase.execSQL("create table xjmusicfavorite  (id integer primary key autoincrement,"+"title varchar(20),"+"singer varchar(20),"+"path varchar (100),"+"ablum varchar (30))");
        sqLiteDatabase.execSQL("create table xjsingerfavorite  (id integer primary key autoincrement,"+"singer varchar(20))");
        sqLiteDatabase.execSQL("create table xjablumfavorite  (id integer primary key autoincrement,"+"ablum varchar(20))");
        //sqLiteDatabase.execSQL("create table xjmusicstatus (id integer primary key autoincrement,"+"status varchar(2))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public ArrayList<Music> getlist(){
        dbr=getReadableDatabase();
        ArrayList<Music> musics=new ArrayList<Music>();
        Cursor cursor= dbr.rawQuery("select * from xjmusiclocal",null);
        String s=null;
        while (cursor.moveToNext())
        {
            Music music=new Music(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            musics.add(music);
        }
        dbr.close();
        return musics;

    }
    public void add(Music music)
    {
        dbw=getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", music.title);
        value.put("singer", music.singer);
        value.put("path", music.path);
        value.put("ablum", music.album);
        dbw.insert("xjmusiclocal", null, value);
        dbw.close();
    }
    /*public boolean musicdbstatus(){
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select * from musicstatus",null);
        String s=cursor.getString(1);
        dbr.close();
        if(s==null){
            dbw=getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("status","y");
            dbw.insert("xjmusicstatus",null,values);
            dbw.close();
            return false;
        }
        else return true;
    }*/
    public Music serach(int id)
    {
        Music music=null;
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select * from xjmusiclocal where id=?",new String[]{String.valueOf(id)});
        while ( cursor.moveToNext()){
            music=new Music(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        }
        dbr.close();
        return music;
    }
    public int exequery(){
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select count(*) from xjmusiclocal",null);
        cursor.moveToNext();
        int i=Integer.parseInt(cursor.getString(0));
        dbr.close();
        return i;
    }
    public Music serach(String title)
    {
        Music music=null;
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select * from xjmusiclocal where id=?",new String[]{title});
        while ( cursor.moveToNext()){
            music=new Music(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        }
        dbr.close();
        return music;
    }
    public ArrayList<Music> getlistbycollect(String collectname){
        ArrayList<Music> musics=new ArrayList<>();
        Music music;
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select * from xjmusiclocal where collectby=?",new String[]{collectname});
        while ( cursor.moveToNext()){
            music=new Music(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            musics.add(music);
        }
        dbr.close();
        return musics;
    }
    private ArrayList<String> musictitle(){
        dbr=getReadableDatabase();
        ArrayList<String> musicnamelist=new ArrayList<>();
        String musicname=null;
        Cursor cursor=dbr.rawQuery("select musictitle from xjmusiclocal",null);
        while (cursor.moveToNext()){
            musicname=cursor.getString(0);
            musicnamelist.add(musicname);
        }
        cursor.close();
        dbr.close();
        return musicnamelist;
    }
    public ArrayList<String> getcollects(){
        dbr=getReadableDatabase();
        Cursor cursor;
        ArrayList<String> musicollectclist=new ArrayList<>();
        String[] s=null;
        for (int i=0;i<musictitle().size();i++){
            cursor=dbr.rawQuery("select collectby from xjmusiclocal where title=?",new String[]{musictitle().get(i)});
            cursor.moveToNext();
            String music=cursor.getString(0);
            s=music.split("[\\(\\)]");
            cursor.close();
        }
        for (int i=0;i<s.length;i++) {
            if(i%2!=0) {
                musicollectclist.add(s[i]);
            }
        }
        dbr.close();
        return musicollectclist;
    }
    public ArrayList<Music> getMusicbycollect(String collectname){
        dbr=getReadableDatabase();
        collectname="("+collectname+")";
        ArrayList<Music> musiclist=new ArrayList<>();
        try {
            Cursor cursor = dbr.rawQuery("select * from xjmusiclocal where collectby like?", new String[]{"%" + collectname + "%"});
            Music music;
            while (cursor.moveToNext()) {
                music = new Music(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                musiclist.add(music);
            }
        dbr.close();
        cursor.close();
        }
        catch (Exception e){

        }
        return musiclist;
    }
    public void Addtodefaultcollect(String name){
        dbw=getWritableDatabase();
        dbr=getReadableDatabase();
        Cursor cursor=dbr.rawQuery("select collectby from xjmusiclocal where title=?",new String[]{name});
        cursor.moveToNext();
        String musicbelongto=cursor.getString(0);
        cursor.close();
        dbr.close();
        ContentValues value = new ContentValues();
        value.put("collectby","我的收藏");
        dbw.update("xjmusiclocal",value,"title=?",new String[]{name});
        dbw.close();

    }

}
