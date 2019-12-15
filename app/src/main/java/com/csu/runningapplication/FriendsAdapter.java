package com.csu.runningapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Friends> {
    private int resourceId;

    public FriendsAdapter(Context context, int resource, List<Friends> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Friends friends=getItem(position); //获取当前项的实例

        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView FriendsName=(TextView)view.findViewById(R.id.friends_name);
        CircleImageView FriendsImg=(CircleImageView)view.findViewById(R.id.friends_image);
        TextView content1=(TextView)view.findViewById(R.id.friends_something);
        if (friends.getcontent().equals("null"))
        { content1.setText("");
        } else {content1.setText(friends.getcontent());}

        FriendsName.setText(friends.getName());
        Glide.with(getContext())
                .load(friends.getImg())
                .into(FriendsImg);



        return view;
    }

}
