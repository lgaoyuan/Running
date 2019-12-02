package com.csu.runningapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.csu.runningapplication.Chat;
import com.csu.runningapplication.ChatAdapter;
import com.csu.runningapplication.Chat_dynamicActivity;
import com.csu.runningapplication.FriendsActivity;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.MyListViewUtils;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.ChatFetch;
import com.csu.runningapplication.http.MyFetch;
import com.csu.runningapplication.jsonbean.JsonBean;
import com.csu.runningapplication.jsonbean.MyJsonBean;


import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements MyListViewUtils.LoadListener {

    private List<Chat> chatlist=new ArrayList<>();
    private Button add;
    private TextView guanzhu;
    private TextView gonglue;
    private TextView yugao;
    private ListView listView;
    private TextView bottom;
    private FragmentManager fm;
    private ImageView friends;
    private MyListViewUtils listViewUtils;
    private ChatAdapter adapter;
    private JsonBean test;
    private MyJsonBean test1;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.chat_fragment,parents,false);
        test=new ChatFetch().fetchItems("9999");

        initchat();
        bottom=v.findViewById(R.id.bottom);
        friends=(ImageView)v.findViewById(R.id.friends);
        adapter=new ChatAdapter(getContext(),R.layout.chat_item,chatlist);
//        listView=(ListView)v.findViewById(R.id.list_view);
        listViewUtils=(MyListViewUtils)v.findViewById(R.id.list_view);
        listViewUtils.setInteface(this);
        listViewUtils.setAdapter(adapter);
//        listView.setAdapter(adapter);
        guanzhu=v.findViewById(R.id.guanzhu);
        gonglue=v.findViewById(R.id.gonglue);
        yugao=v.findViewById(R.id.yugao);
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatAdapter adapter=new ChatAdapter(getContext(),R.layout.chat_item,chatlist);

                bottom.setText("  ____");
                fm=getActivity().getSupportFragmentManager();
                Fragment fragment=fm.findFragmentById(R.id.chat_fragment);
                if(fragment==null){
                    fragment=new ChatFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                }
            }
        });
        gonglue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listViewUtils.setAdapter(null);
                bottom.setText("               ____");
                //嵌入fragment
//                fm=getActivity().getSupportFragmentManager();
//                Fragment fragment=fm.findFragmentById(R.id.runfragment);
//                if(fragment==null){
//                    fragment=new RunFragment();
//                    fm.beginTransaction().replace(R.id.fragmentContainer1,fragment).commit();
//                }

            }
        });
        yugao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listViewUtils.setAdapter(null);
                bottom.setText("                           ____");
            }
        });
        add=(Button)v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开一个发消息的页面 类似说说？
                Intent i=new Intent(getContext(), Chat_dynamicActivity.class);
                startActivity(i);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), FriendsActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
    public void initchat(){
        Chat chat=new Chat("活力中南就是很有活力，费厂的有活力，真正的帧的诱惑里，我是一个不知道说啥的不知道干嘛的帖子，我真的不知道我是哪里来的帖子。",R.drawable.flyimg,"飞飞飞飞");
        Chat chat1=new Chat("活力中南就是很有活力，费厂的有活力，真正的帧的诱惑里，我是一个不知道说啥的不知道干嘛的帖子，我真的不知道我是哪里来的帖子。",R.drawable.flyimg,"飞飞飞飞",R.drawable.chat_img);
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat1);
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat);
    }
    //获得bitmap
    //ImageView.setImageBitmap(bitmap);
    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Chat chat1=new Chat("活力中南就是很有活力，费厂的有活力，真正的帧的诱惑里，我是一个不知道说啥的不知道干嘛的帖子，我真的不知道我是哪里来的帖子。",R.drawable.flyimg,"飞飞飞飞",R.drawable.chat_img);
                chatlist.add(chat1);
                adapter.notifyDataSetChanged();
                listViewUtils.loadComplete();
            }
        },1000);

    }

    @Override
    public void PullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Chat chat1=new Chat("活力中南就是很有活力，费厂的有活力，真正的帧的诱惑里，我是一个不知道说啥的不知道干嘛的帖子，我真的不知道我是哪里来的帖子。",R.drawable.flyimg,"飞飞飞飞",R.drawable.chat_img);
                chatlist.add(chat1);
                adapter.notifyDataSetChanged();
                listViewUtils.loadComplete();
            }
        },1000);


    }


}
