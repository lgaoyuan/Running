package com.csu.runningapplication.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.csu.runningapplication.Chat;
import com.csu.runningapplication.ChatAdapter;
import com.csu.runningapplication.Chat_dynamicActivity;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<Chat> chatlist=new ArrayList<>();
    private Button add;
    private TextView guanzhu;
    private TextView gonglue;
    private TextView yugao;
    private ListView listView;
    private TextView bottom;
    private FragmentManager fm;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.chat_fragment,parents,false);
        initchat();
        bottom=v.findViewById(R.id.bottom);
        ChatAdapter adapter=new ChatAdapter(getContext(),R.layout.chat_item,chatlist);
        listView=(ListView)v.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
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


                bottom.setText("               ____");

                fm=getActivity().getSupportFragmentManager();
                Fragment fragment=fm.findFragmentById(R.id.runfragment);
                if(fragment==null){
                    fragment=new RunFragment();
                    fm.beginTransaction().replace(R.id.fragmentContainer1,fragment).commit();
                }

            }
        });
        yugao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setAdapter(null);
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
}
