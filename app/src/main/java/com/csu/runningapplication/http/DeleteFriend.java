package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class DeleteFriend extends SingleHttpFetch{
    public String fetchItems(String id,String hisid) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/deleteFriend").buildUpon().appendQueryParameter("userid", id).appendQueryParameter("hisid",hisid).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
