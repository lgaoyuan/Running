package com.csu.runningapplication;

import android.app.Application;


public class MyApplication extends Application {
    private int image1;

    public void setImage1(int image1){
        this.image1=image1;

    }
    public int getImage1(){
        return  this.image1;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
