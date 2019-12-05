package com.example.shortmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    TextView textView;
    Button button;
    static   Myadapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list);
        textView=findViewById(R.id.item_text);
        button=findViewById(R.id.str);
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter =new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver,filter);
        permission();
        myadapter=new Myadapter(MainActivity.this);
        listView.setAdapter(myadapter);

    }
private void permission(){
    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS},1);
}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            Toast.makeText(MainActivity.this,"获权成功",Toast.LENGTH_LONG).show();

        }

    }
    private ArrayList<String> getmes(){
        ContentResolver contentsolver=MainActivity.this.getContentResolver();
        Uri uri=Uri.parse("content://sms/inbox");
        ArrayList<String> arrayList=new ArrayList<>();
        Cursor cursor=contentsolver.query(uri,null,null,null,null);
        while (cursor.moveToNext())
        {

            arrayList.add(cursor.getString(cursor.getColumnIndex("body")));

        }
return arrayList;

    }
    public class Myadapter extends BaseAdapter{
        public LayoutInflater layoutInflater;
        public Myadapter(Context context){
            layoutInflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return getmes().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=layoutInflater.inflate(R.layout.meslayout,viewGroup,false);
            TextView textView=view.findViewById(R.id.item_text);
            textView.setText(getmes().get(i));

            return view;
        }
    }
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String format=intent.getStringExtra("format");
            if(bundle!=null)
            {
                Object[]  pd=(Object[])bundle.get("pdus");
                for (Object object:pd) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu( (byte[])object,format);
                    button.setText(smsMessage.getOriginatingAddress());
                    Toast.makeText(context,smsMessage.getMessageBody(),Toast.LENGTH_LONG).show();
                    button.setText(smsMessage.getMessageBody());
                }

            }

            myadapter=new Myadapter(MainActivity.this);
            MainActivity.listView.setAdapter(myadapter);
            Toast.makeText(context,"来短信",Toast.LENGTH_LONG).show();
        }
    }

}
