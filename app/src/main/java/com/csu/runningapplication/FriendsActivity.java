package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.csu.runningapplication.http.AddFriend;
import com.csu.runningapplication.http.SearchFriend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends AppCompatActivity {
    private EditText search;
    private TextView search_button;
    private String str;
    private TextView friendsid;
    private LinearLayout lin;
    private Button add;
    private MyApplication application;
    private String ID;
    private String count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        search=(EditText)findViewById(R.id.search_friends);
        friendsid=(TextView)findViewById(R.id.friends_id);
        search_button=(TextView) findViewById(R.id.search_button);
        add=(Button)findViewById(R.id.friends_add);
        lin=(LinearLayout)findViewById(R.id.friends_lin);
        application=(MyApplication)getApplication();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               str=search.getText().toString();
               new ChatItemsTask().execute();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addItemsTask().execute();

            }
        });


    }

    private class ChatItemsTask extends AsyncTask<Void, Void, String> {
        String mj;
        @Override
        protected String doInBackground(Void... params) {
            mj = new SearchFriend().fetchItems(application.getUserid(),str);
            return mj;
        }

        @Override
        protected void onPostExecute(String  result) {// 执行完毕后，则更新UI
            if (result.equals("[]")) {
                Toast.makeText(FriendsActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                lin.setVisibility(View.INVISIBLE);
                return;
            }
            if (result.equals("")) {
                Toast.makeText(FriendsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }

            lin.setVisibility(View.VISIBLE);
            try {
                JSONArray json = new JSONArray(result);
                for(int i=0;i<json.length();i++)
                {
                    JSONObject jb=json.getJSONObject(i);
                    friendsid.setText(jb.getString("name"));
                    ID=jb.getString("studentid");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class addItemsTask extends AsyncTask<Void, Void, String>{
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj=new AddFriend().fetchItems(application.getUserid(),ID);
            return mj;
        }
        @Override
        protected void onPostExecute(String  result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(FriendsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                return;

            }
            if(result.equals("0")){
                Toast.makeText(FriendsActivity.this,"发送请求成功",Toast.LENGTH_LONG).show();
            }else if(result.equals("1")){
                Toast.makeText(FriendsActivity.this,"已发送请求",Toast.LENGTH_LONG).show();
            }else if(result.equals("2")){
                Toast.makeText(FriendsActivity.this,"已添加该好友",Toast.LENGTH_LONG).show();
            }

        }
    }
}
