/*
 * 设置标签
 * */
package com.csu.runningapplication.http;

import android.net.Uri;
import java.io.IOException;


public class SetTagFetch extends SingleHttpFetch {

    public void fetchItems(String tag, String userid) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/setTag").buildUpon().appendQueryParameter("tag", tag).appendQueryParameter("userid", userid).build().toString();
        try {
            getUrl(url);
        } catch (IOException e) {
        }
    }

}
