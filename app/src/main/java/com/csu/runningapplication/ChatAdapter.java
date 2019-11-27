package com.csu.runningapplication;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private int resourceId;
    private MyApplication app;
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
        Button  ChatButton1=(Button)view.findViewById(R.id.guanzhu1);
        ImageView ChatImage1=(ImageView)view.findViewById(R.id.chat_img);
        ChatImage.setImageResource(chat.getImage());
        ChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),Dialog.class);
                int ss=R.drawable.flyimg;
                i.putExtra("data",ss);
                getContext().startActivity(i);

            }
        });

        ChatName.setText(chat.getName());
        ChatName1.setText(chat.getName1());
        ChatImage1.setImageResource(chat.getImage1());
        ChatImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),Dialog.class);
                int ss=R.drawable.chat_img;
                i.putExtra("data",ss);
                getContext().startActivity(i);
            }
        });
        return view;
    }
}
