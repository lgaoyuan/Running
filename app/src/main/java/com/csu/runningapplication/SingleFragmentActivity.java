package com.csu.runningapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.csu.runningapplication.ui.ChatFragment;

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.runfragment);
        if(fragment==null){
            fragment=new ChatFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }

        View mChat=findViewById(R.id.nav_chat);
        mChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment=fm.findFragmentById(R.id.chatfragment);
                if(fragment==null){
                    fragment=new ChatFragment();
                    fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
        View mRun=findViewById(R.id.nav_chat);
        mRun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment=fm.findFragmentById(R.id.runfragment);
                if(fragment==null){
                    fragment=new ChatFragment();
                    fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
        View mMy=findViewById(R.id.nav_chat);
        mMy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment=fm.findFragmentById(R.id.myfragment);
                if(fragment==null){
                    fragment=new ChatFragment();
                    fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
    }
}

