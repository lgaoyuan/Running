package com.csu.runningapplication;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {
    private int image1;
    private String userid="000000";
    private String password="33333333";
    private int type=0;//跑步或骑车，默认跑步
    private String id="45";
    private String id_guanzhu="45";
    private String id_yugao="45";
    private int weight=55;




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

    public void setId(String id){this.id=id;}

    public String getId(){return id;}

    public void setId_guanzhu(String id){this.id_guanzhu=id;}

    public String getId_guanzhu(){return  id_guanzhu;}

    public void setId_yugao(String id){this.id_yugao=id;}

    public String getId_yugao(){return id_yugao;}

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
