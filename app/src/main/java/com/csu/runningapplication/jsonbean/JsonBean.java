package com.csu.runningapplication.jsonbean;

import java.util.List;

public class JsonBean {
    /**
     * id : 1
     * userid : 000000
     * name : 我是1号
     * avatarUrl : http://abc.com
     * text : 1
     * date : 2019-11-27T02:24:09.000+0000
     * imgUrl : [{"imgurl":"111"},{"imgurl":"222"}]
     */

    private int id;
    private String userid;
    private String name;
    private String avatarUrl;
    private String text;
    private String date;
    private List<ImgUrlBean> imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public List<ImgUrlBean> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<ImgUrlBean> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static class ImgUrlBean {
        /**
         * imgurl : 111
         */

        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
