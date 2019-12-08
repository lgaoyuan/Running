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


import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RunningActivity extends Activity {

    private static final String TAG = "RunningActivity";
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";
    private MyApplication myApplication;
    private Button mStart;
    private Button mStop;
    private Button mPause;
    private TextView mIsRunning;

    private AMapTrackClient aMapTrackClient;
    private Timer timer;
    private Boolean isTimerRunning = false;//定时器控制

    private long terminalId;
    private long trackId;
    private boolean isServiceRunning;
    private boolean isGatherRunning;
    private double d;//此轨迹实时里程
    private double allD = 0;//累计里程

    private long startTime;
    private int useTime = 0;//耗时

    private TextView mSpeed;
    private TextView mTime;
    private TextView mDis;
    private TextView mCal;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {//路程
                mDis.setText(msg.obj.toString());
            } else if (msg.what == 1001) {//时间
                mTime.setText(msg.obj.toString());
            } else if (msg.what == 1002) {//配速
                mSpeed.setText(msg.obj.toString());
            } else if (msg.what == 1003) {//配速
                mCal.setText(msg.obj.toString());
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
                //Toast.makeText(RunningActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                //开始采集
                aMapTrackClient.setTrackId(trackId);
                aMapTrackClient.startGather(onTrackListener);
            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                //Toast.makeText(RunningActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(RunningActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(RunningActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                //Toast.makeText(RunningActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(RunningActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
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
        myApplication=(MyApplication) getApplication();
        mSpeed = (TextView) findViewById(R.id.speed_text);
        mTime = (TextView) findViewById(R.id.time_text);
        mDis = (TextView) findViewById(R.id.distance);
        mCal = (TextView) findViewById(R.id.calorie_text);

        //载入定时器
        timeController();
        //设置按钮
        mPause = findViewById(R.id.pause_button);
        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause.setVisibility(View.GONE);
                mStart.setVisibility(View.VISIBLE);
                mStop.setVisibility(View.VISIBLE);

                aMapTrackClient.stopGather(onTrackListener);//停止采集
                aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, terminalId), onTrackListener);

                isTimerRunning = false;
                allD = allD + d;//将最新轨迹里程加入累计
                d = 0;
                mIsRunning.setText("跑步暂停");
            }
        });
        mStart = findViewById(R.id.start_button);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStart.setVisibility(View.GONE);
                mStop.setVisibility(View.GONE);
                mPause.setVisibility(View.VISIBLE);

                startTrack();

                isTimerRunning = true;//启动定时器
                mIsRunning.setText("跑步进行中...");
            }
        });
        mStop = findViewById(R.id.stop_button);
        //初始显示的信息
        mIsRunning = (TextView) findViewById(R.id.isRunning);
        mIsRunning.setText("跑步进行中...");
        mStart.setVisibility(View.GONE);
        mStop.setVisibility(View.GONE);

        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(1, 5);

        startTrack();
        startTime = System.currentTimeMillis();

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //停止
                if (timer != null) {
                    timer.cancel();
                }
                Intent i = new Intent(RunningActivity.this, TrackSearchActivity.class);
                i.putExtra("startTime", startTime);
                i.putExtra("distance", allD);//(单位:m)
                i.putExtra("time", useTime);
                i.putExtra("speed", mSpeed.getText());
                i.putExtra("calorie", mCal.getText());
                startActivity(i);
                RunningActivity.this.finish();
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
                                    isTimerRunning = true;
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
                                    isTimerRunning = true;
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


    private void myDistance() {
        // 查询行驶里程
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
                                            d = tracks.get(0).getDistance();
                                        }
                                    } else {
                                        Toast.makeText(RunningActivity.this, "未获取到轨迹", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RunningActivity.this, "查询轨迹失败，" + queryTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
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
    }

    /*
     * 定时器控制
     * */
    private void timeController() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isTimerRunning) {
                    Message msg = new Message();
                    msg.what = 1000;
                    myDistance();
                    msg.obj = String.format("%.2f", (allD + d) / 1000);//路程，保留两位小数
                    handler.sendMessage(msg);

                    Message msg1 = new Message();
                    msg1.what = 1001;
                    useTime++;
                    int s = useTime % 60;
                    int m = useTime / 60;
                    String ss = String.format("%02d", s);
                    String mm = String.format("%02d", m);
                    msg1.obj = mm + ":" + ss;//时间
                    handler.sendMessage(msg1);

                    Message msg2 = new Message();
                    msg2.what = 1002;
                    if (allD + d != 0) {//判断时间（分母）是否为0
                        int speed = (int) (useTime / ((allD + d) / 1000));
                        s = speed % 60;
                        m = speed / 60;
                    } else {
                        s = 0;
                        m = 0;
                    }
                    ss = String.format("%02d", s);
                    msg2.obj = m + "'" + ss + "''";//配速
                    handler.sendMessage(msg2);

                    Message msg3 = new Message();
                    msg3.what = 1003;
                    double k=1;
                    if(myApplication.getType()==0){
                        k=1;
                    }else if(myApplication.getType()==1){
                        k=0.62;
                    }
                    double c=myApplication.getWeight()*((allD + d) / 1000)*k;
                    msg3.obj=String.format("%.2f", c);//卡路里
                    handler.sendMessage(msg3);
                }
            }
        }, 0, 1000);
    }
}
