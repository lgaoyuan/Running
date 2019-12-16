package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class JoinAct extends SingleHttpFetch {
    public String fetchItems(String id,String userid) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/joinAct").buildUpon().appendQueryParameter("id", id).appendQueryParameter("userid",userid).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
