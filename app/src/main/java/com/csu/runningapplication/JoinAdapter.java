package com.csu.runningapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class JoinAdapter extends ArrayAdapter<Join> {
    private int resourceId;
    public JoinAdapter(@NonNull Context context, int resource, List<Join> objects) {
        super(context, resource,objects);
        resourceId=resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        Join join=getItem(position);
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




        return view;

    }
}

