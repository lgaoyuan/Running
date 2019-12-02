package com.csu.runningapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.csu.runningapplication.http.MileageFetch;
import com.csu.runningapplication.http.TrackFetch;
import com.csu.runningapplication.util.Constants;
import com.csu.runningapplication.util.SimpleOnTrackListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 轨迹查询示例
 * 1、演示如何查询终端最近上报的轨迹点
 * 2、演示如何查询终端最近上报的轨迹点及其所属轨迹信息，分别绘制出每条轨迹下的所有轨迹点
 */
public class TrackSearchActivity extends Activity {

    private AMapTrackClient aMapTrackClient;
    private MyApplication myApplication;

    private TextureMapView textureMapView;
    private TextView mDistance;
    private TextView mSpeed;
    private TextView mTime;
    private TextView mCalorie;
    private List<Polyline> polylines = new LinkedList<>();
    private List<Marker> endMarkers = new LinkedList<>();

    private double distance;
    private int time;
    private String calorie;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_search);
        myApplication=(MyApplication) getApplication();

        aMapTrackClient = new AMapTrackClient(getApplicationContext());

        Intent i = getIntent();
        startTime = i.getLongExtra("startTime", System.currentTimeMillis());

        distance=i.getDoubleExtra("distance",0);//数据展示
        mDistance=findViewById(R.id.distance_summary);
        mDistance.setText(String.format("%.2f", distance / 1000));

        String speed=i.getStringExtra("speed");
        mSpeed=findViewById(R.id.speed_summary);
        mSpeed.setText(speed);

        time=i.getIntExtra("time",0);
        int s = time % 60;
        int m = time / 60;
        String ss = String.format("%02d", s);
        String mm = String.format("%02d", m);
        mTime=findViewById(R.id.time_summary);
        mTime.setText(mm+":"+ss);

        calorie=i.getStringExtra("calorie");
        mCalorie=findViewById(R.id.calorie_summary);
        mCalorie.setText(calorie);

        textureMapView = findViewById(R.id.activity_track_search_map);
        textureMapView.onCreate(savedInstanceState);

        new TrackItemsTask().execute();//上传数据

        //clearTracksOnMap();
        // 先查询terminal id，然后用terminal id查询轨迹
        // 查询符合条件的所有轨迹，并分别绘制
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, Constants.TERMINAL_NAME), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(final QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        long tid = queryTerminalResponse.getTid();
                        // 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
                        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
                                Constants.SERVICE_ID,
                                tid,
                                -1,     // 轨迹id，不指定，查询所有轨迹，注意分页仅在查询特定轨迹id时生效，查询所有轨迹时无法对轨迹点进行分页
                                startTime,
                                System.currentTimeMillis(),
                                0,      // 不启用去噪
                                0,   // 绑路
                                0,      // 不进行精度过滤
                                DriveMode.DRIVING,  // 当前仅支持驾车模式
                                0,     // 距离补偿
                                5000,   // 距离补偿，只有超过5km的点才启用距离补偿
                                1,  // 结果应该包含轨迹点信息
                                1,  // 返回第1页数据，但由于未指定轨迹，分页将失效
                                100    // 一页不超过100条
                        );
                        aMapTrackClient.queryTerminalTrack(queryTrackRequest, new SimpleOnTrackListener() {
                            @Override
                            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
                                if (queryTrackResponse.isSuccess()) {
                                    List<Track> tracks = queryTrackResponse.getTracks();
                                    if (tracks != null && !tracks.isEmpty()) {
                                        boolean allEmpty = true;
                                        for (Track track : tracks) {
                                            List<Point> points = track.getPoints();
                                            if (points != null && points.size() > 0) {
                                                allEmpty = false;
                                                drawTrackOnMap(points);
                                            }
                                        }
                                        if (allEmpty) {
                                            Toast.makeText(TrackSearchActivity.this,
                                                    "所有轨迹都无轨迹点，请尝试放宽过滤限制，如：关闭绑路模式", Toast.LENGTH_SHORT).show();
                                        } else {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("共查询到").append(tracks.size()).append("条轨迹，每条轨迹行驶距离分别为：");
                                            for (Track track : tracks) {
                                                sb.append(track.getDistance()).append("m,");
                                            }
                                            sb.deleteCharAt(sb.length() - 1);
                                            Toast.makeText(TrackSearchActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(TrackSearchActivity.this, "你的运动时间太短啦", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(TrackSearchActivity.this, "查询历史轨迹失败，" + queryTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(TrackSearchActivity.this, "Terminal不存在", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNetErrorHint(queryTerminalResponse.getErrorMsg());
                }
            }
        });


    }

    private void showNetErrorHint(String errorMsg) {
        Toast.makeText(this, "网络请求失败，错误原因: " + errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void drawTrackOnMap(List<Point> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);
        if (points.size() > 0) {
            // 起点
            Point p = points.get(0);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
        }
        if (points.size() > 1) {
            // 终点
            Point p = points.get(points.size() - 1);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
        }
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        Polyline polyline = textureMapView.getMap().addPolyline(polylineOptions);
        polylines.add(polyline);
        textureMapView.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
    }

    @Override
    protected void onPause() {
        super.onPause();
        textureMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textureMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        textureMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textureMapView.onDestroy();
    }

    /*
     * http保存
     * */
    private class TrackItemsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return new TrackFetch().fetchItems(myApplication.getUserid(),String.valueOf(distance),String.valueOf(time),calorie,String.valueOf(myApplication.getType()),String.valueOf(startTime));
        }

        @Override
        protected void onPostExecute(String result){// 执行完毕
            if(!result.equals("1")){
                Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
