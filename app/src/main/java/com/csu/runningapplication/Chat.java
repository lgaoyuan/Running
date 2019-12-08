package com.csu.runningapplication;

import android.widget.Button;
import android.widget.ImageView;

public class Chat {
    private String name;
    private int image;
    private String name1;
    private int image1;

    public Chat(String name,int image,String name1){
        this.name=name;
        this.image=image;
        this.name1=name1;

    }
    public Chat(String name,int image,String name1,int image1){
        this.name=name;
        this.image=image;
        this.name1=name1;
        this.image1=image1;

    }
    public Chat(String name,String name1){
        this.name=name;
        this.name1=name1;
    }
    public String getName(){
        return name;
    }
    public int getImage(){
        return image;
    }
    public String getName1(){
        return  name1;
    }
    public int getImage1(){return  image1;}
}
