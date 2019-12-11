package com.csu.runningapplication.http;

import android.net.Uri;

import com.csu.runningapplication.jsonbean.IdJsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class NewBbsFetch extends SingleHttpFetch {
    public IdJsonBean fetchItems(String userid, String text){
        String url = Uri.parse("https://lgaoyuan.xyz:8080/running/uploadBbs").buildUpon().appendQueryParameter("userid", userid).appendQueryParameter("text", text).build().toString();
        String json = null;
        try {
            json = getUrl(url);
        } catch (IOException e) {
            return null;
        }
        return GsonItems(json);
    }

    public IdJsonBean GsonItems(String json) {
        Type listType=new TypeToken<List<IdJsonBean>>() {}.getType();
        List<IdJsonBean> gson = new Gson().fromJson(json, listType);
        return gson.get(0);
    }
}
