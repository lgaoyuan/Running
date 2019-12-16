package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.csu.runningapplication.http.CancleAct;

public class Join_Dialog extends Activity {
    private TextView text1;
    private TextView text2;
    private String cancelid;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_dialog);
        text1=findViewById(R.id.cancel_text1);
        text2=findViewById(R.id.cancel_text2);
        application=(MyApplication)getApplication();
        Intent intent=getIntent();
        cancelid=intent.getStringExtra("cancleid");

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancleActTask().execute();
                onBackPressed();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private class CancleActTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj = new CancleAct().fetchItems(cancelid,application.getUserid());
            return mj;
        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();

    }
}
