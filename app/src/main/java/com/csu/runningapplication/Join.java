package com.csu.runningapplication;

public class Join {
    private String text;
    private String date;
    private String url;
    private String id;
    private String is;

    public Join(String text,String date,String url,String id){
        this.text=text;
        this.date=date;
        this.url=url;
        this.id=id;
    }
    public Join(String text,String date,String url,String id,String is){
        this.text=text;
        this.date=date;
        this.url=url;
        this.id=id;
        this.is=is;
    }

    public String getDate() {
        return date;
    }
    public String getText(){
        return text;
    }
    public String getUrl(){
        return url;
    }
    public String getId(){return  id;}
    public String getIs(){return  is;}
}
