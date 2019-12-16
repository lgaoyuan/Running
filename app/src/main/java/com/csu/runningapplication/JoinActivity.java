package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.csu.runningapplication.http.GetMyNotice;
import com.csu.runningapplication.http.JoinAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class JoinActivity extends AppCompatActivity {
    private MyApplication application;
    private JoinAdapter adapter;
    private List<Join> joinlist = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        application = (MyApplication) getApplication();
        listView = findViewById(R.id.Join_listview);
        adapter = new JoinAdapter(this, R.layout.jion_item, joinlist);
        listView.setAdapter(adapter);

    }
    @Override
    protected void onResume(){
        super.onResume();
        joinlist.clear();
        new GetMyActivityTask().execute();
    }

    private class GetMyActivityTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj = new GetMyNotice().fetchItems(application.getUserid());
            return mj;
        }

        @Override
        protected void onPostExecute(String result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(JoinActivity.this, "网络连接失败", LENGTH_SHORT).show();
                return;

            }
            if (result.equals("[]")) {
                Toast.makeText(JoinActivity.this, "你还没有参加任何活动", LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray json = new JSONArray(result);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jb = json.getJSONObject(i);
                    JSONArray json1=new JSONArray(jb.getString("act"));
                    JSONObject jb1=json1.getJSONObject(0);
                    Join join=new Join(jb1.getString("text"),jb1.getString("date"),null,jb.getString("actid"),jb1.getString("is"));
                    joinlist.add(join);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
