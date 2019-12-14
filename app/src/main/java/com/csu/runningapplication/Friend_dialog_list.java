package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csu.runningapplication.http.DeleteFriend;

public class Friend_dialog_list extends Activity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView hisname;
    private MyApplication application;
    private String hisid;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_dialog_list);
        btn1=findViewById(R.id.dialog_list_btn1);
        btn2=findViewById(R.id.dialog_list_btn2);
        btn3=findViewById(R.id.dialog_list_btn3);
        hisname=findViewById(R.id.dialog_list_name);
        application=(MyApplication)getApplication();
        Intent i=getIntent();
        hisid=i.getStringExtra("id");
        name=i.getStringExtra("name");
        hisname.setText(name);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Friend_dialog_list.this,Friend_own.class);
                i.putExtra("hisid",hisid);
                startActivity(i);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FriendDeleteTask().execute();
                onBackPressed();
            }
        });



        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private class FriendDeleteTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj=new DeleteFriend().fetchItems(application.getUserid(),hisid);
            return mj;
        }
    }
}
