package com.csu.runningapplication.http;

import android.net.Uri;
import java.io.IOException;

public class RunningFetch extends SingleHttpFetch {
    public void fetchItems(String userid, String password) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/userMessage").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("password", password).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return;
        }
        return;
    }
}
