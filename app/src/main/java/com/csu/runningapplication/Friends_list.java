package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.csu.runningapplication.http.FriendList;
import com.csu.runningapplication.http.NewFriend;
import com.csu.runningapplication.http.SearchFriend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Friends_list extends AppCompatActivity {
    private List<Friends> friendslist=new ArrayList<>();
    private List<Friends> friendslist1=new ArrayList<>();
    private ListView list;
    private ListView list1;
    private MyApplication application;
    private FriendsAdapter adapter;
    private FriendsAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        list=findViewById(R.id.friends_list);
        list1=findViewById(R.id.friends_list1);
        application=(MyApplication)getApplication();
        new NewFriendsItemsTask().execute();
        new FriendListItemsTask().execute();
        adapter=new FriendsAdapter(this,R.layout.friendlist_item,friendslist);
        adapter1=new FriendsAdapter(this,R.layout.friendlist_item,friendslist1);
        list.setAdapter(adapter);
        list1.setAdapter(adapter1);

    }

    private class NewFriendsItemsTask extends AsyncTask<Void, Void, String> {
        String mj;
        @Override
        protected String doInBackground(Void... params) {
            mj = new NewFriend().fetchItems(application.getUserid());
            return mj;
        }

        @Override
        protected void onPostExecute(String  result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(Friends_list.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                System.out.println(application.getUserid());
                return;

            }
            System.out.println(application.getUserid());
            System.out.println("查找成功");
            System.out.println(result);
            try {
                JSONArray json = new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    JSONObject jb=json.getJSONObject(i);
                    Friends friends=new Friends(jb.getString("name"));
                    System.out.println(jb.getString("name"));
                    friendslist.add(friends);
                    adapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private class FriendListItemsTask extends AsyncTask<Void, Void, String> {
        String mj;
        @Override
        protected String doInBackground(Void... params) {
            mj = new FriendList().fetchItems(application.getUserid());
            return mj;
        }

        @Override
        protected void onPostExecute(String  result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(Friends_list.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                System.out.println(application.getUserid());
                return;

            }
            System.out.println(result);
            try {
                JSONArray json = new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    JSONObject jb=json.getJSONObject(i);
                    Friends friends=new Friends(jb.getString("name"));
                    System.out.println(jb.getString("name"));
                    friendslist1.add(friends);
                    adapter1.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
