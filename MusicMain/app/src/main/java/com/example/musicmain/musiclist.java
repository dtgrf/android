package com.example.musicmain;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link musiclist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link musiclist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class musiclist extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    Myadapter myAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public musiclist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment musiclist.
     */
    // TODO: Rename and change types and number of parameters
    public static musiclist newInstance(String param1, String param2) {
        musiclist fragment = new musiclist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CollectList collectList=new CollectList(getActivity());

        myAdapter=new Myadapter(getActivity(),  collectList.GetCollectList("我的收藏"));
        View view=View.inflate(getActivity(),R.layout.fragment_musiclist,null);
        listView=view.findViewById(R.id.musiclists);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  path = local_detail.musiclists.get(i).path;
                if (!TextUtils.isEmpty(path)) {//判断字符非空
                    local_detail.mybinder.change(path);
                    initseekbar();
                    updateprogress();
                    if (!local_detail.mybinder.isplay())
                        local_detail.startorstop.setBackgroundResource(R.mipmap.pause);
                    else if (local_detail.mybinder.isplay())
                        local_detail.startorstop.setBackgroundResource(R.mipmap.play);
                } else {
                }
            }
        });
      myAdapter.setOnItemDeleteClickListener(new Myadapter.onItemSelectListener() {
          @Override
          public void onSelectClick(View view1) {
              CollectList collectList=new CollectList(getActivity());
              collectList.Addtodefault("我的收藏");
              ImageView imageView=view1.findViewById(R.id.favorite_status);
              imageView.setImageResource(R.mipmap.favorited);
          }
      });
        return view;
    }
  /*  @Override
    public void onSelectClick(View view) {
        Log.e("ssdd","覅u受到核辐射的覅u时代覅是否会i苏菲bui ");
    }*/



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void initseekbar(){
        int musicwidth=local_detail.mybinder.getMusicWidth();
        local_detail.seekBar.setMax(musicwidth);
    }
    static   void updateprogress(){
        guide.thread=new Thread(){
            @Override
            public void run() {
                while (!interrupted())//判断线程中断状态
                {
                    int currentposition=local_detail.mybinder.getcurrentposition();
                    Message message=Message.obtain();//创建对象，从缓存池返回一个实例，避免重复；
                    message.obj=local_detail.seekBar;
                    message.arg1=currentposition;
                    message.what=1;
                    local_detail.hander.sendMessage(message);
                }
            }
        };

        guide.thread.start();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
