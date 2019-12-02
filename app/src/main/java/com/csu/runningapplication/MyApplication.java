package com.csu.runningapplication;

import android.app.Application;


public class MyApplication extends Application {
    private int image1;
    private String userid="000000";
    private String password="33333333";
    private int type=0;//跑步或骑车，默认跑步

    public void setImage1(int image1){
        this.image1=image1;

    }
    public int getImage1(){
        return  this.image1;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
