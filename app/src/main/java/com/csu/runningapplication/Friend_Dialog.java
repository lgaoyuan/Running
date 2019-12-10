package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csu.runningapplication.http.AgreeFriend;


public class Friend_Dialog extends AppCompatActivity {
    private TextView id;
    private Button btn1;
    private Button btn2;
    private MyApplication application;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__dialog);
        id=findViewById(R.id.dialog_id);
        btn1=findViewById(R.id.dialog_button1);
        btn2=findViewById(R.id.dialog_button2);
        application=(MyApplication)getApplication();
        Intent i=getIntent();
        name=i.getStringExtra("id");
        id.setText(i.getStringExtra("name"));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AgreeFriendsItemsTask().execute();
                onBackPressed();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private class AgreeFriendsItemsTask extends AsyncTask<Void, Void, String> {
        String mj;
        @Override
        protected String doInBackground(Void... params) {
            mj = new AgreeFriend().fetchItems(application.getUserid(),name);
            return mj;
        }



    }
}
