package com.csu.runningapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.model.LatestPointRequest;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.csu.runningapplication.R;
import com.csu.runningapplication.RunningActivity;
import com.csu.runningapplication.util.Constants;
import com.csu.runningapplication.util.SimpleOnTrackListener;

public class RunFragment extends Fragment {

    private AMapTrackClient aMapTrackClient;
    private TextureMapView mapView;

    private Marker locationMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.run_fragment, parents, false);

        Button mStartButton = (Button) v.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RunningActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

}
