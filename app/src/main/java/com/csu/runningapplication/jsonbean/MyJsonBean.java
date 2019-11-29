package com.csu.runningapplication.jsonbean;

import java.util.List;

public class MyJsonBean {
    /**
     * bbsnum : 0
     * friends : 0
     * running : 0.0
     * cycling : 0.0
     * data : [45,10,5]
     */

    private String bbsnum;
    private String friends;
    private double running;
    private double cycling;
    private List<Double> data;

    public String getBbsnum() {
        return bbsnum;
    }

    public void setBbsnum(String bbsnum) {
        this.bbsnum = bbsnum;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
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

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
}
