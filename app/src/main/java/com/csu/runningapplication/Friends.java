package com.csu.runningapplication;

public class Friends {
    private String name;
    private String id;
    private String img;
    private String autograph;
    private String content="";

    public Friends(String name){this.name=name;}
    public Friends(String name,String id,String img){this.name=name;this.id=id;this.img=img;}
    public Friends(String name,String id,String img,String content){this.name=name;this.id=id;this.img=img;this.content=content;}
    public String getName(){return name;}
    public String getId(){return id;}
    public String getImg(){return img;}
    public String getAutograph(){return autograph;}
    public String getcontent(){return  content;}

}
