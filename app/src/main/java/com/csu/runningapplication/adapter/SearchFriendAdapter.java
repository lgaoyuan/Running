package com.csu.runningapplication.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.csu.runningapplication.CircleImageView;
import com.csu.runningapplication.Friends;
import com.csu.runningapplication.FriendsActivity;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.AddFriend;

import java.util.List;

public class SearchFriendAdapter extends ArrayAdapter<Friends> {
    private int resourceId;
    private MyApplication application;
    private Friends friends;
    public SearchFriendAdapter(@NonNull Context context, int resource, @NonNull List<Friends> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        //name img id content
        friends=getItem(position);
        application=(MyApplication)getContext().getApplicationContext();
        CircleImageView imageView=view.findViewById(R.id.friends_image);
        TextView friendid=view.findViewById(R.id.friends_id);
        TextView content=view.findViewById(R.id.friend_content);
        Button button=view.findViewById(R.id.friends_add);

        Glide.with(getContext())
                .load(friends.getImg())
                .into(imageView);
        friendid.setText(friends.getName());
        content.setText(friends.getcontent());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new addItemsTask().execute();
            }
        });


        return view;
    }

    private class addItemsTask extends AsyncTask<Void, Void, String> {
        String mj;

        @Override
        protected String doInBackground(Void... voids) {
            mj=new AddFriend().fetchItems(application.getUserid(),friends.getId());
            return mj;
        }
        @Override
        protected void onPostExecute(String  result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                return;

            }
            if(result.equals("0")){
                Toast.makeText(getContext(),"发送请求成功",Toast.LENGTH_LONG).show();
            }else if(result.equals("1")){
                Toast.makeText(getContext(),"已发送请求",Toast.LENGTH_LONG).show();
            }else if(result.equals("2")){
                Toast.makeText(getContext(),"已添加该好友",Toast.LENGTH_LONG).show();
            }

        }
    }

}
