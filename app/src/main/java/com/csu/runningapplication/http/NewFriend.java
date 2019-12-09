package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class NewFriend extends SingleHttpFetch{
    public String fetchItems(String id) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/newFriend").buildUpon().appendQueryParameter("userid", id).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}