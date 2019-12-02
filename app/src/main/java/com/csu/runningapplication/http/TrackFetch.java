package com.csu.runningapplication.http;

import android.net.Uri;
import java.io.IOException;

public class TrackFetch extends SingleHttpFetch {
    public String fetchItems(String userid, String distance,String time,String calorie,String type,String dates) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/sendRunning").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("distance", distance).appendQueryParameter("time", time).appendQueryParameter("calorie", calorie).appendQueryParameter("type", type).appendQueryParameter("dates", dates).build().toString();
        String result = null;
        try {
            result = getUrl(url);
        } catch (IOException e) {
            return null;
        }
        return result;
    }
}
