package com.csu.runningapplication;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.csu.runningapplication.ui.ChatFragment;
import com.csu.runningapplication.ui.MyFragment;
import com.csu.runningapplication.ui.RunFragment;


public class MainActivity extends FragmentActivity {

    private FragmentManager fm;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Fragment fragment=fm.findFragmentById(R.id.myfragment);
                if(fragment==null){
                    fragment=new MyFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }
            }
        });

    }

}
