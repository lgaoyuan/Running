package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class MileageFetch extends SingleHttpFetch {
    public double fetchItems(String userid,int type){
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getMileage").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("type", String.valueOf(type)).build().toString();
        String str = null;
        try {
            str = getUrl(url);
        } catch (IOException e) {
            return -1;
        }
        return Double.parseDouble(str);
    }
}
