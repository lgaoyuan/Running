package com.csu.runningapplication.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csu.runningapplication.Join;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.GetMyNotice;
import com.csu.runningapplication.http.JoinAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class GetActivityAdapter extends ArrayAdapter<Join> {
    private int resourceId;
    private MyApplication application;
    private Join join;
    private String id;

    public GetActivityAdapter(Context context, int resource, List<Join> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        join = getItem(position);
        application = (MyApplication) getContext().getApplicationContext();
        TextView textViewdate = (TextView) view.findViewById(R.id.Join_date);
        TextView textViewtext = (TextView) view.findViewById(R.id.Join_text);
        ImageView imageViewJoin = (ImageView) view.findViewById(R.id.Join_Img);
        textViewdate.setText(join.getDate());
        textViewtext.setText(join.getText());
        RequestOptions requestOptions = new RequestOptions()
                .error(null);
        Glide.with(getContext())
                .load(join.getUrl())
                .apply(requestOptions)
                .into(imageViewJoin);

        final Button button = (Button) view.findViewById(R.id.Join_button);
        button.setText("参加");
        if(join.getIs().equals("0")){
            button.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Join join = getItem(position);
                id = join.getId();
                new JoinActTask().execute();


            }
        });
        return view;
    }

    private class JoinActTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj = new JoinAct().fetchItems(id, application.getUserid());
            return mj;
        }

        @Override
        protected void onPostExecute(String result) {// 执行完毕后，则更新UI
            if (result.equals("-1")) {
                Toast.makeText(getContext().getApplicationContext(), "您已参加过该活动", LENGTH_SHORT).show();
                return;
            }else if(result.equals("1")){
                Toast.makeText(getContext().getApplicationContext(), "参加成功", LENGTH_SHORT).show();
                return;
            }else {
                Toast.makeText(getContext().getApplicationContext(), "网络连接失败", LENGTH_SHORT).show();
                return;
            }

        }
    }
}
