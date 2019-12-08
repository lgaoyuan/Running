package com.csu.runningapplication.jsonbean;

import java.util.List;

public class JsonBean_gonglue {
    /**
     * id : 19
     * text : 1
     * date : 2019-11-27T02:24:09.000+0000
     * imgUrl : []
     */

    private int id;
    private String text;
    private String date;
    private List<?> imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<?> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<?> imgUrl) {
        this.imgUrl = imgUrl;
    }
}
