package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.csu.runningapplication.http.FriendList;
import com.csu.runningapplication.http.NewFriend;
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
        adapter=new FriendsAdapter(this,R.layout.friendlist_item,friendslist);
        adapter1=new FriendsAdapter(this,R.layout.friendlist_item,friendslist1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Friends_list.this,Friend_Dialog.class);
                        intent.putExtra("count","0");
                        intent.putExtra("id",friendslist.get(i).getId());
                        intent.putExtra("name",friendslist.get(i).getName());
                        friendslist.remove(i);
                        adapter.notifyDataSetChanged();
                        startActivity(intent);

            }
        });
        list1.setAdapter(adapter1);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Friends_list.this,Friend_dialog_list.class);
                intent.putExtra("id",friendslist1.get(i).getId());
                intent.putExtra("name",friendslist1.get(i).getName());
                friendslist1.remove(i);
                adapter.notifyDataSetChanged();
                startActivity(intent);

            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        friendslist.clear();
        friendslist1.clear();
        new NewFriendsItemsTask().execute();
        new FriendListItemsTask().execute();
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
                return;

            }
            System.out.println(result);
            try {
                JSONArray json = new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    JSONObject jb=json.getJSONObject(i);
                    Friends friends=new Friends(jb.getString("name"),jb.getString("fromid"),null);
                    friendslist.add(friends);



                }
                adapter.notifyDataSetChanged();
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
                return;

            }
            try {
                JSONArray json = new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    JSONObject jb=json.getJSONObject(i);
                    Friends friends=new Friends(jb.getString("name"),jb.getString("studentid"),jb.getString("url"));
                    friendslist1.add(friends);


                }
                adapter1.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
