package com.csu.runningapplication;

public class Friends {
    private String name;
    private String id;

    public Friends(String name){this.name=name;}
    public Friends(String name,String id){this.name=name;this.id=id;}
    public String getName(){return name;}
    public String getId(){return id;}
}
