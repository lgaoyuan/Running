/*
 * 获取标签
 * */
package com.csu.runningapplication.http;

import android.net.Uri;
import java.io.IOException;


public class TagsFetch extends SingleHttpFetch {

    public String fetchItems() {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getAllTags").buildUpon().build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return null;
        }
        return json;
    }

}
