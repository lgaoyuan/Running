package com.csu.runningapplication;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceRequest;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.csu.runningapplication.util.Constants;
import com.csu.runningapplication.util.SimpleOnTrackLifecycleListener;
import com.csu.runningapplication.util.SimpleOnTrackListener;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RunningActivity extends Activity {

    private static final String TAG = "RunningActivity";
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

    private AMapTrackClient aMapTrackClient;
    private Timer timer;

    private long terminalId;
    private long trackId;
    private boolean isServiceRunning;
    private boolean isGatherRunning;


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1000) {
                TextView dis=(TextView)findViewById(R.id.distance);
                dis.setText(msg.obj.toString()+"m");
            }
        }
    };


    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
                Toast.makeText(RunningActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                //开始采集
                aMapTrackClient.setTrackId(trackId);
                aMapTrackClient.startGather(onTrackListener);
            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                Toast.makeText(RunningActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                
            } else {
                Log.w(TAG, "error onStartTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(RunningActivity.this,
                        "error onStartTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                Toast.makeText(RunningActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = false;
                isGatherRunning = false;

            } else {
                Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(RunningActivity.this,
                        "error onStopTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                Toast.makeText(RunningActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                //定时显示
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1000;
                        msg.obj=myDistance();
                        handler.sendMessage(msg);
                        Log.d("distanceMsg",msg.obj.toString());
                    }
                }, 0, 1000);
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                Toast.makeText(RunningActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(RunningActivity.this,
                        "error onStartGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                Toast.makeText(RunningActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = false;
                
            } else {
                Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
                Toast.makeText(RunningActivity.this,
                        "error onStopGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_layout);



        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(1, 5);

        startTrack();

        Button trackButton=(Button)findViewById(R.id.activity_search_location);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //停止服务
                aMapTrackClient.stopGather(onTrackListener);
                aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, terminalId), onTrackListener);
                if(timer!=null) {
                    timer.cancel();
                }
                Intent i=new Intent(RunningActivity.this,TrackSearchActivity.class);
                i.putExtra("trackId",trackId);
                startActivity(i);
            }
        });
    }


    private void startTrack() {
        // 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
        // 用Terminal ID开启轨迹服务
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, Constants.TERMINAL_NAME), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        // 当前终端已经创建过，直接使用查询到的terminal id
                        terminalId = queryTerminalResponse.getTid();
                        aMapTrackClient.addTrack(new AddTrackRequest(Constants.SERVICE_ID, terminalId), new SimpleOnTrackListener() {
                            @Override
                            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                            if (addTrackResponse.isSuccess()) {
                                // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
                                trackId = addTrackResponse.getTrid();
                                TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    trackParam.setNotification(createNotification());
                                }
                                aMapTrackClient.startTrack(trackParam, onTrackListener);
                            } else {
                                Toast.makeText(RunningActivity.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            }
                            }
                        });
                        
                    } else {
                        // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
                        aMapTrackClient.addTerminal(new AddTerminalRequest(Constants.TERMINAL_NAME, Constants.SERVICE_ID), new SimpleOnTrackListener() {
                            @Override
                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                                if (addTerminalResponse.isSuccess()) {
                                    terminalId = addTerminalResponse.getTid();
                                    TrackParam trackParam = new TrackParam(Constants.SERVICE_ID, terminalId);
                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        trackParam.setNotification(createNotification());
                                    }
                                    aMapTrackClient.startTrack(trackParam, onTrackListener);
                                } else {
                                    Toast.makeText(RunningActivity.this, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RunningActivity.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        Intent nfIntent = new Intent(RunningActivity.this, RunningActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(RunningActivity.this, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceRunning) {
            aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, terminalId), new SimpleOnTrackLifecycleListener());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private double myDistance(){
        // 查询行驶里程
        /*final double[] d = new double[1];

        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.SERVICE_ID, Constants.TERMINAL_NAME), new SimpleOnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    long terminalId = queryTerminalResponse.getTid();
                    if (terminalId > 0) {
                        long curr = System.currentTimeMillis();
                        DistanceRequest distanceRequest = new DistanceRequest(
                                Constants.SERVICE_ID,
                                terminalId,
                                curr - 12 * 60 * 60 * 1000, // 开始时间
                                curr,   // 结束时间
                                trackId  // 轨迹id
                        );
                        aMapTrackClient.queryDistance(distanceRequest, new SimpleOnTrackListener() {
                            @Override
                            public void onDistanceCallback(DistanceResponse distanceResponse) {
                                if (distanceResponse.isSuccess()) {
                                    d[0] =distanceResponse.getDistance();
                                } else {
                                    //appendLogText("行驶里程查询失败，" + distanceResponse.getErrorMsg());
                                }
                            }
                        });
                    } else {
                        //appendLogText("终端不存在，请先使用轨迹上报示例页面创建终端和上报轨迹");
                    }
                } else {
                    //appendLogText("终端查询失败，" + queryTerminalResponse.getErrorMsg());
                }
            }
        });
        return d[0];*/

        final double[] d = new double[1];

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
                                trackId,     // 轨迹id，不指定，查询所有轨迹，注意分页仅在查询特定轨迹id时生效，查询所有轨迹时无法对轨迹点进行分页
                                System.currentTimeMillis() - 12 * 60 * 60 * 1000,
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
                                            }
                                        }
                                        if (allEmpty) {
                                        } else {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("共查询到").append(tracks.size()).append("条轨迹，每条轨迹行驶距离分别为：");
                                            for (Track track : tracks) {
                                                d[0] =track.getDistance();
                                                sb.append(track.getDistance()).append("m,");
                                            }
                                            sb.deleteCharAt(sb.length() - 1);
                                            Toast.makeText(RunningActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RunningActivity.this, "未获取到轨迹", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RunningActivity.this, "查询历史轨迹失败，" + queryTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RunningActivity.this, "Terminal不存在", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });
        return d[0];
    }
}
