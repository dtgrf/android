package com.example.musicmain;

public class Music {
    String title;
    String singer;
    String path;
    int id;
    String length;
    String album;
    Music(String title,String singer,String path,String album){
        this.album=album;
        this.title=title;
        this.singer=singer;
        this.path=path;
    }
    Music(int id,String title,String singer,String path,String album){
        this.id=id;
        this.album=album;
        this.title=title;
        this.singer=singer;
        this.path=path;

    }
}
