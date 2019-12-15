package com.csu.runningapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.csu.runningapplication.ui.ChatFragment;
import com.csu.runningapplication.ui.MyFragment;
import com.csu.runningapplication.ui.RunFragment;


public class MainActivity extends FragmentActivity {

    private FragmentManager fm;
    private ImageView chat;
    private TextView chatname;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chat=findViewById(R.id.chat);
        chatname=findViewById(R.id.chat_name);
        fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.runfragment);
        if(fragment==null){
            fragment=new RunFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }

        View mChat=findViewById(R.id.nav_chat);
        mChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chat.setImageResource(R.drawable.ic_chatclick);
                chatname.setTextColor(Color.parseColor("#05b6f8"));
                Fragment fragment=fm.findFragmentById(R.id.chat_fragment);
                if(fragment==null){
                    fragment=new ChatFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
        View mRun=findViewById(R.id.nav_run);
        mRun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chat.setImageResource(R.drawable.ic_chat);
                chatname.setTextColor(Color.parseColor("#ffffff"));
                Fragment fragment=fm.findFragmentById(R.id.runfragment);
                if(fragment==null){
                    fragment=new RunFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
        View mMy=findViewById(R.id.nav_my);
        mMy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chat.setImageResource(R.drawable.ic_chat);
                chatname.setTextColor(Color.parseColor("#ffffff"));
                Fragment fragment=fm.findFragmentById(R.id.myfragment);
                if(fragment==null){
                    fragment=new MyFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }
            }
        });

    }

}
