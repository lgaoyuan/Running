package com.csu.runningapplication.http;

import android.content.Context;
import android.net.Uri;
import java.io.IOException;

public class EchartsFetch extends SingleHttpFetch {
    public String fetchItems(String userid, String type, String start, String end) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/echartsMessage").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("type", type).appendQueryParameter("start", start).appendQueryParameter("end", end).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}
