package com.csu.runningapplication.jsonbean;

public class MyJsonBean {

    /**
     * bbsnum : 1
     * friends : 2
     * running : 10.0
     * cycling : 5.0
     * time : 1000
     */

    private int bbsnum;
    private int friends;
    private double running;
    private double cycling;
    private int time;

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

    public double getRunning() {
        return running;
    }

    public void setRunning(double running) {
        this.running = running;
    }

    public double getCycling() {
        return cycling;
    }

    public void setCycling(double cycling) {
        this.cycling = cycling;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
