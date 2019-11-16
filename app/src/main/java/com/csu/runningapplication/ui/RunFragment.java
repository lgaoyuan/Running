package com.csu.runningapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.csu.runningapplication.R;

public class RunFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.run_fragment,parents,false);
        return v;
    }
}
