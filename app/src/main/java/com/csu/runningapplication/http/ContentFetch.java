/*
 * 登录
 * */
package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class ContentFetch extends SingleHttpFetch {

    public void fetchItems(String userid, String content) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/changeContent").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("content", content).build().toString();
        try {
            getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
