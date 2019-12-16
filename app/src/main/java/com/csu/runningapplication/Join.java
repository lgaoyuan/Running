package com.csu.runningapplication;

public class Join {
    private String text;
    private String date;
    private String url;

    public Join(String text,String date,String url){
        this.text=text;
        this.date=date;
        this.url=url;
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
}
