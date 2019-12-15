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


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private int resourceId;
    private String head;
    public ChatAdapter(Context context, int textViewResourceId, List<Chat> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Chat chat=getItem(position);           //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView ChatImage=(ImageView)view.findViewById(R.id.chat_image);
        TextView ChatName=(TextView) view.findViewById(R.id.chat_name);
        TextView ChatName1=(TextView)view.findViewById(R.id.name1);
        ImageView ChatImage1=(ImageView)view.findViewById(R.id.chat_img);
        TextView Chatdate=(TextView)view.findViewById(R.id.chat_date);
        Chatdate.setText(chat.getChatdate());
        TextView Chatnumber=(TextView)view.findViewById(R.id.chat_number);

            if (!chat.getNumber().equals("0") && !chat.getNumber().equals("1")&&!chat.getNumber().equals("")) {
                Chatnumber.setText("点开查看更多");

            }

        head=chat.getUri();

        //图片加载失败时，显示的图片
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.user_192);
        //头像
        Glide.with(getContext())
                .load(head)
                .apply(requestOptions)
                .into(ChatImage);

        ChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat1=getItem(position);
                Intent i=new Intent(getContext(),Dialog.class);
                i.putExtra("test","0");
                i.putExtra("headimg",chat1.getUri());
                getContext().startActivity(i);


            }
        });



        ChatName.setText(chat.getName());
        ChatName1.setText(chat.getName1());
        //说说图片 加载第一张
        RequestOptions requestOptions1 = new RequestOptions()
                .error(null);
        if(!chat.getImgcount().isEmpty()){
        Glide.with(getContext())
                .load(chat.getImgcount().get(0))
                .apply(requestOptions1)
                .into(ChatImage1);
//        ChatImage1.setMaxHeight(500);
//         if(ChatImage1.getHeight()>1000){
//             ChatImage1.setMaxHeight(800);
//         }

        ChatImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat2=getItem(position);
                Intent i=new Intent(getContext(),Dialog.class);
                i.putExtra("test","1");
                i.putStringArrayListExtra("imglist", (ArrayList<String>) chat2.getImgcount());
                getContext().startActivity(i);
            }
        });
        }
        return view;
    }
}
