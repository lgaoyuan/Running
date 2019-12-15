package com.csu.runningapplication.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csu.runningapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_dialog extends Fragment {
    private ImageView img;
    private int ID;
    private String IDuri;


    public Chat_dialog(int ImgID) {
        // Required empty public constructor
        this.ID=ImgID;
    }
    public Chat_dialog(String IDuri){
        this.IDuri=IDuri;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_viewpager,container,false);
        img=(ImageView)v.findViewById(R.id.dialog_image);
//        img.setImageResource(ID);
        Glide.with(getContext())
                .load(IDuri)
                .error(null)
                .into(img);
        // Inflate the layout for this fragment
        return v;
    }

}
