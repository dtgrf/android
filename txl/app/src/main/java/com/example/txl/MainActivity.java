package com.example.txl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button button;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.list1);
        listView=findViewById(R.id.xs);
        final Myadapter myadapter=new Myadapter(MainActivity.this);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   listView.setAdapter(myadapter);
                   Toast.makeText(MainActivity.this,String.valueOf(getnameinfo().get(0)),Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(this,getcityinfo().get(0).toString(),Toast.LENGTH_LONG).show();


    }
    private ArrayList<String> getphoneinfo(){
        ArrayList<String> arrayList=new ArrayList<>();
        ContentResolver contentResolver=MainActivity.this.getContentResolver();
        Uri myuri=Uri.parse("ContactsContract.Contacts.CONTENT_URI");


        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        int i=0;
        while (cursor.moveToNext())
        {

           arrayList.add( cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1)))
           ;
        }
        cursor.close();
        return arrayList;

    }
    private ArrayList<String> getcityinfo(){
        ArrayList<String> arrayList=new ArrayList<>();
        ContentResolver contentResolver=MainActivity.this.getContentResolver();
        Uri myuri=Uri.parse("ContactsContract.Contacts.CONTENT_URI");
       // Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        Cursor cursor=contentResolver.query(ContactsContract.Data.CONTENT_URI,null,null,null,null);
        int i=0;
        while (cursor.moveToNext())
        {

            arrayList.add( cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1)));
        }
        cursor.close();
        return arrayList;

    }
    private ArrayList<String> getnameinfo(){
        ArrayList<String> arrayList=new ArrayList<>();
        ContentResolver contentResolver=MainActivity.this.getContentResolver();
        Uri myuri=Uri.parse( "content://contacts/people/");
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        int i=0;
        while (cursor.moveToNext())
        {

            arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        }
        cursor.close();
        return arrayList;

    }

    class  Myadapter extends  BaseAdapter{

        public LayoutInflater layoutInflater;
        public Myadapter(Context context){
            layoutInflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return getnameinfo().size();
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
            view=layoutInflater.inflate(R.layout.txlitem,viewGroup,false);
            TextView textView=view.findViewById(R.id.name);
            textView.setText(getnameinfo().get(i));
            TextView textView1=view.findViewById(R.id.phone);
            textView1.setText(getphoneinfo().get(i));
            TextView textView2=view.findViewById(R.id.city);
            textView2.setText("城市"+i);
            return view;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)//确认权限
                {

                   // Toast.makeText(MainActivity.this,"获取权限",Toast.LENGTH_LONG).show();
                }

            }
            break;
        }
    }
}
