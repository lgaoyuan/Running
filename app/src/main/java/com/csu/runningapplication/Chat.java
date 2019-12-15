package com.csu.runningapplication;


import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String name;
    private int image;
    private String name1;
    private int image1;
    private String uri;
    private List<String> imgcount=new ArrayList<String>();
    private String number="";
    private String chatdate;

    public void addImgcount(String img){
        imgcount.add(img);
    }
    public void setnumber(String number){this.number=number;}

    public Chat(String name,String date){
        this.name=name;
        this.chatdate=date;
    }

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

    public Chat(String name,String uri,String name1,String date){
        this.name=name;
        this.uri=uri;
        this.name1=name1;
        this.chatdate=date;

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
    public String getUri(){return  uri;}
    public List<String> getImgcount(){return imgcount;}
    public String getNumber(){return number;}
    public String getChatdate(){return chatdate;}
}
