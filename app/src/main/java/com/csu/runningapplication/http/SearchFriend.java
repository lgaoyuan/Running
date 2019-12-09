package com.csu.runningapplication.http;


import android.net.Uri;

import com.csu.runningapplication.jsonbean.JsonBean;
import com.csu.runningapplication.jsonbean.MyJsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SearchFriend extends SingleHttpFetch {

    public String fetchItems(String id) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/searchFriend").buildUpon().appendQueryParameter("userid", id).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
