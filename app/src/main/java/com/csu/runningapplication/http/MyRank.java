package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class MyRank extends SingleHttpFetch {
    public String fetchItems(String userid,String startDate, String endDate) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/myRank").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("startDate",startDate).appendQueryParameter("endDate",endDate).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
