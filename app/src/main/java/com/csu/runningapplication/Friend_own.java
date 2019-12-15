package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.csu.runningapplication.http.GetFriendBbs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Friend_own extends AppCompatActivity implements MyListViewUtils.LoadListener {
    private MyListViewUtils list;
    private ChatAdapter adapter;
    private List<Chat> chatlist=new ArrayList<>();
    private String hisid;
    private String Bbsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_own);
        list=findViewById(R.id.own_list);
        list.setInteface(this);
        adapter=new ChatAdapter(this,R.layout.chat_item,chatlist);
        Intent i=getIntent();
        hisid=i.getStringExtra("hisid");
        new GetFriendBbsTask().execute();
//        init();
        list.setAdapter(adapter);


    }
    public void init(){

        Chat chat=new Chat("hello,world","http://106.54.39.17/wp-content/uploads/2019/09/srf.jpg","飞飞飞飞");
        chatlist.add(chat);

    }
//  new Handler().postDelayed(new Runnable() {
    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AddFriendBbsTask().execute();
                list.loadComplete();

            }
        },500);

    }
//
//    @Override
//    public void PullLoad() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                list.loadComplete();
//
//            }
//        },500);
//
//    }

    public class GetFriendBbsTask extends AsyncTask<Void,Void,String>{
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
           mj=new GetFriendBbs().fetchItems("9999",hisid);
           return mj;
        }
        @Override
        protected void onPostExecute(String result){
            if (result == null) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(result);
            try {
                JSONArray json=new JSONArray(result);
                for(int i=0;i<json.length();i++){
                    JSONObject jb=json.getJSONObject(i);
                    Bbsid=jb.getString("id");
                    Chat chat=new Chat(jb.getString("text"),jb.getString("url"),jb.getString("name"));
                    chatlist.add(chat);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class AddFriendBbsTask extends AsyncTask<Void,Void,String>{
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj=new GetFriendBbs().fetchItems(Bbsid,hisid);
            return mj;
        }
        @Override
        protected void onPostExecute(String result){
            if (result.equals("[]")) {
                Toast.makeText(getApplicationContext(), "网络连接失败或已显示全部内容", Toast.LENGTH_SHORT).show();
                list.setAdapter(null);
                list.setAdapter(adapter);
                return;
            }
            System.out.println(Bbsid+"123456");
            System.out.println(result);
            try {
                JSONArray json=new JSONArray(result);
                for(int i=0;i<json.length();i++){
                    JSONObject jb=json.getJSONObject(i);
                    Bbsid=jb.getString("id");
                    Chat chat=new Chat(jb.getString("text"),jb.getString("url"),jb.getString("name"));
                    chatlist.add(chat);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
