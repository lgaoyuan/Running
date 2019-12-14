/*
 * 登录
 * */
package com.csu.runningapplication.http;

import android.net.Uri;
import java.io.IOException;

public class LoginFetch extends SingleHttpFetch {

    public int fetchItems(String userid, String name, String password) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/login").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("name", name).appendQueryParameter("password", password).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return 0;
        }
        return Integer.parseInt(json);
    }
}
