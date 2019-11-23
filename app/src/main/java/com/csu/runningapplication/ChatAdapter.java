package com.csu.runningapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private int resourceId;
    public ChatAdapter(Context context, int textViewResourceId, List<Chat> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Chat chat=getItem(position);           //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView ChatImage=(ImageView)view.findViewById(R.id.chat_image);
        TextView ChatName=(TextView) view.findViewById(R.id.chat_name);
        TextView ChatName1=(TextView)view.findViewById(R.id.name1);
        ChatImage.setImageResource(chat.getImage());
        ChatName.setText(chat.getName());
        ChatName1.setText(chat.getName1());
        return view;
    }
}
