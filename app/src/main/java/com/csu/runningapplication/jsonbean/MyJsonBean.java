package com.csu.runningapplication.jsonbean;

public class MyJsonBean {

    /**
     * bbsnum : 1
     * friends : 2
     * running : 10000
     * cycling : 5000
     * time : 1000
     * calorie : 0
     */
    private String url;
    private int bbsnum;
    private int friends;
    private int running;
    private int cycling;
    private int time;
    private int calorie;
    private String content;
    private int actnum;
    private String tag;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getActnum() {
        return actnum;
    }

    public void setActnum(int actnum) {
        this.actnum = actnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBbsnum() {
        return bbsnum;
    }

    public void setBbsnum(int bbsnum) {
        this.bbsnum = bbsnum;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public int getCycling() {
        return cycling;
    }

    public void setCycling(int cycling) {
        this.cycling = cycling;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getAvatarUrl() {
        return url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.url = avatarUrl;
    }
}
