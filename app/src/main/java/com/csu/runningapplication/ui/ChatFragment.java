package com.csu.runningapplication.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.csu.runningapplication.Chat;
import com.csu.runningapplication.ChatAdapter;
import com.csu.runningapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<Chat> chatlist=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.chat_fragment,parents,false);
        TextView tv=(TextView)v.findViewById(R.id.guanzhu);
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initchat();
        ChatAdapter adapter=new ChatAdapter(getContext(),R.layout.chat_item,chatlist);
        ListView listView=(ListView)v.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return v;
    }
    public void initchat(){
        Chat chat=new Chat("活力中南就是很有活力，费厂的有活力，真正的帧的诱惑里，我是一个不知道说啥的不知道干嘛的帖子，我真的不知道我是哪里来的帖子。",R.drawable.w100,"飞飞飞飞");
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat);
        chatlist.add(chat);
    }
}
