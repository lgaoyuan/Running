package com.csu.runningapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.csu.runningapplication.Join_Dialog;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.CancleAct;
import com.csu.runningapplication.http.GetMyNotice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class JoinAdapter extends ArrayAdapter<Join> {
    private int resourceId;
    private MyApplication application;
    private Join join;
    public JoinAdapter(@NonNull Context context, int resource, List<Join> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        join=getItem(position);
        application=(MyApplication)getContext().getApplicationContext();
        TextView textViewdate=(TextView)view.findViewById(R.id.Join_date);
        TextView textViewtext=(TextView)view.findViewById(R.id.Join_text);
        ImageView imageViewJoin=(ImageView)view.findViewById(R.id.Join_Img);
        textViewdate.setText(join.getDate());
        textViewtext.setText(join.getText());
        RequestOptions requestOptions = new RequestOptions()
                .error(null);
        Glide.with(getContext())
                .load(join.getUrl())
                .apply(requestOptions)
                .into(imageViewJoin);


        Button button=(Button)view.findViewById(R.id.Join_button);
        if(join.getIs().equals("0")){
            button.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Join join1=getItem(position);
               Intent intent=new Intent(getContext(), Join_Dialog.class);
               intent.putExtra("cancleid",join1.getId());
               getContext().startActivity(intent);
            }
        });



        return view;

    }



    }


