/*
 * 获取“我的”页面信息
 * */
package com.csu.runningapplication.http;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.csu.runningapplication.jsonbean.MyJsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MyFetch extends SingleHttpFetch {

    public MyJsonBean fetchItems(String userid, String password) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/userMessage").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("password", password).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return null;
        }
        return GsonItems(json);
    }

    public MyJsonBean GsonItems(String json) {
        Type listType=new TypeToken<List<MyJsonBean>>() {}.getType();
        List<MyJsonBean> gson = new Gson().fromJson(json, listType);
        return gson.get(0);
    }
}
