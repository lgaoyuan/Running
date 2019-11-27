package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Dialog extends AppCompatActivity {
    private MyApplication bpp;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        Intent intent=getIntent();
        int ss=intent.getIntExtra("data",R.drawable.chat_img);



        image=findViewById(R.id.imageview1);
        image.setImageResource(ss);
    }
}
