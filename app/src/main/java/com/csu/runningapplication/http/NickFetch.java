/*
 * 登录
 * */
package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class NickFetch extends SingleHttpFetch {

    public void fetchItems(String userid, String content) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/changeNick").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("name", content).build().toString();
        try {
            getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
