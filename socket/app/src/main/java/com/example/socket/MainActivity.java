package com.example.socket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;
    String text;
    String rec;
    Socket socket;
    BufferedWriter out;
    BufferedReader in;
    StringBuilder s1;
    ArrayList<String> re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.go);
        textView=findViewById(R.id.show);
        editText=findViewById(R.id.edit);
        socket=new Socket();
        s1=new StringBuilder();
         final Thread thread = new Thread(runnable);
         thread.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread1=new Thread(runnable1);
                thread1.start();

            }
        });

    }

    private String getcon () throws IOException {
        socket.connect(new InetSocketAddress("10.127.106.172",2000), 1000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s=null;
        char [] b=new char[1024];
        int i=0;
        while (in.read(b)!=-1){
            s=new String(b)+"\n";
        s1.append(s);
        textView.setText(s1.toString()+"\n");}
        in.close();
        return s1.toString();

    }

    private void  send(String s) throws IOException {
        try {
            socket.connect(new InetSocketAddress("10.127.106.172",2000), 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(s);
        out.newLine();
        out.flush();

    }
   public Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Message message1=new Message();
                /*try {
                    socket=new Socket("192.168.190.1",2000);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                try {

                    while (true)
                    {
                        rec = getcon();}
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                message1.obj = rec;
                message1.what = 0;
                hander.sendMessage(message1);

        }
    };
    public Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            try {

                    //socket1=new Socket("192.168.190.1",2000);
                    text = editText.getText().toString();
                    Log.i("xxx",text);
                    send(text);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public Handler hander=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
           // if(msg.obj!=null)
            //textView.setText((msg.obj.toString()));
        }
    };



}
