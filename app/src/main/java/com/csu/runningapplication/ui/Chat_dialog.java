package com.csu.runningapplication.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csu.runningapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_dialog extends Fragment {
    private ImageView img;
    private int ID;


    public Chat_dialog(int ImgID) {
        // Required empty public constructor
        this.ID=ImgID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_viewpager,container,false);
        img=(ImageView)v.findViewById(R.id.dialog_image);
        img.setImageResource(ID);
        // Inflate the layout for this fragment
        return v;
    }

}
