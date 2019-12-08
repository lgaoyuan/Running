package com.csu.runningapplication.http;



import android.net.Uri;

import com.csu.runningapplication.jsonbean.JsonBean_gonglue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Chat_yugaoFetch extends  SingleHttpFetch{
    public String fetchItems(String id) {
    String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getNotice").buildUpon().appendQueryParameter("id", id).build().toString();
    String json = null;
    try {
        json = getUrl(url);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return json;
}
    public JsonBean_gonglue GsonItems(String json) {
        Type listType=new TypeToken<List<JsonBean_gonglue>>() {}.getType();
        List<JsonBean_gonglue> gson = new Gson().fromJson(json, listType);
        return gson.get(0);
    }
    public JsonBean_gonglue fetchItemsPull(String id) {
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/getNotice").buildUpon().appendQueryParameter("id", id).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GsonItems(json);
    }
}
