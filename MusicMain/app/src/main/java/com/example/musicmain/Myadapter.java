package com.example.musicmain;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Myadapter extends BaseAdapter {
    ArrayList<Music> musiclists;
    private LayoutInflater layoutInflater;
    ImageView imageview;
    Myadapter(Context context, ArrayList<Music> list) {
        this.musiclists = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getCount() {
        int i = musiclists.size();
        return i;
    }

    @Override
    public Object getItem(int i) {
        return musiclists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView( int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.musicitem, null, false);
        TextView t = view.findViewById(R.id.music_item_text);
        imageview=view.findViewById(R.id.favorite_status);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectListener.onSelectClick(view);
            }
        });
        TextView t1 = view.findViewById(R.id.music_item_singer);
        StringBuilder stringBuilder0 = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" " + musiclists.get(i).title);
        stringBuilder0.append("  " + musiclists.get(i).singer + " ");
        t.setText(stringBuilder.toString());
        t1.setText(stringBuilder0.toString());
        return view;
    }

    public interface onItemSelectListener {
        void onSelectClick(View view);
    }

    private onItemSelectListener mOnItemSelectListener;

    public void setOnItemDeleteClickListener(onItemSelectListener monItemSelectClickListener) {
        this.mOnItemSelectListener=monItemSelectClickListener;
    }

}
