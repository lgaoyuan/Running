package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class GetMyNotice extends SingleHttpFetch {
    public String fetchItems(String userid) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getMyNotice").buildUpon().appendQueryParameter("userid",userid).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
