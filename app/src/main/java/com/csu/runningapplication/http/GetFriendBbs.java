package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class GetFriendBbs extends  SingleHttpFetch {
    public String fetchItems(String id,String hisid) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getFriendBbs").buildUpon().appendQueryParameter("id", id).appendQueryParameter("userid",hisid).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
