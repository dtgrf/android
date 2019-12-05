
package com.example.musicmain;

import android.content.Context;

import java.util.ArrayList;

public class CollectList {
    String CollectListName;

    ArrayList<ArrayList<Music>> Collectmusiclist;
    Context context;
    MyMusicSqlite myMusicSqlite;
    CollectList( Context context){
        //this.CollectListName=collectListName;
        this.context=context;
        myMusicSqlite=new MyMusicSqlite(context,"mymusicdb",null,1);
    }
    public void InitList(){
        ArrayList<Music>musiclist;
        ArrayList<String> collects=myMusicSqlite.getcollects();
        for (int i=0;i<collects.size();i++) {
           musiclist= myMusicSqlite.getMusicbycollect(collects.get(i));
           Collectmusiclist.add(musiclist);
        }
    }
    public ArrayList<Music> GetCollectList(String Collectname){
        return myMusicSqlite.getMusicbycollect(Collectname);

    }
    public void Addtodefault(String name){
        myMusicSqlite.Addtodefaultcollect(name);

    }
}

