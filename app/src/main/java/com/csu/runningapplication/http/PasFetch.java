/*
 * 登录
 * */
package com.csu.runningapplication.http;

import android.net.Uri;

import java.io.IOException;

public class PasFetch extends SingleHttpFetch {

    public int fetchItems(String userid, String oldPas, String password) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/resetPas").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("oldPas", oldPas).appendQueryParameter("password", password).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return 0;
        }
        return Integer.parseInt(json);
    }
}
