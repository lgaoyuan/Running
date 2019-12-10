package com.csu.runningapplication;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
        FriendsName.setText(friends.getName());
//        Button FriendsButton=(Button)view.findViewById(R.id.friends_agree);


        return view;
    }

}
