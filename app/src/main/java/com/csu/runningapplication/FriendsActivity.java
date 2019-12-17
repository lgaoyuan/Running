package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csu.runningapplication.adapter.SearchFriendAdapter;
import com.csu.runningapplication.http.AddFriend;
import com.csu.runningapplication.http.SearchFriend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private EditText search;
    private TextView search_button;
    private String str;
    private MyApplication application;
    private SearchFriendAdapter adapter;
    private List<Friends> friendsList = new ArrayList<>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        search = (EditText) findViewById(R.id.search_friends);
        search_button = (TextView) findViewById(R.id.search_button);
        listView = findViewById(R.id.search_list);
        adapter = new SearchFriendAdapter(FriendsActivity.this, R.layout.friend_item, friendsList);
        listView.setAdapter(adapter);
        application = (MyApplication) getApplication();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = search.getText().toString();
                if (!str.equals("")) {
                    friendsList.clear();
                    new SearchItemsTask().execute();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        friendsList.clear();
        //推荐好友
    }

    private class SearchItemsTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... params) {
            mj = new SearchFriend().fetchItems(application.getUserid(), str);
            return mj;
        }

        @Override
        protected void onPostExecute(String result) {// 执行完毕后，则更新UI
            if (result.equals("[]")) {
                Toast.makeText(FriendsActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.equals("")) {
                Toast.makeText(FriendsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println("输出结果" + result);
            try {
                JSONArray json = new JSONArray(result);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jb = json.getJSONObject(i);
                    Friends friends = new Friends(jb.getString("name"), jb.getString("studentid"), jb.getString("avatarUrl"), jb.getString("content"));
                    friendsList.add(friends);

                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
