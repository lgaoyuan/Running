package com.csu.runningapplication;



import java.text.SimpleDateFormat;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csu.runningapplication.R;

/**
 *
 * @author 自定义listview实现上拉刷新和下拉加载
 * 2018-08-25 刘明昆 q571039838
 *
 */


import java.text.SimpleDateFormat;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 *
 * @author 自定义listview实现上拉刷新和下拉加载
 * 2018-08-25 刘明昆 q571039838
 *
 */
public class MyListViewUtils extends ListView implements AbsListView.OnScrollListener{

    private View bottomview; //尾文件
    private View headview; //头文件
    private int totaItemCounts;//用于表示是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private LoadListener loadListener; //接口回调
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int Yload;//位置
    boolean isLoading;//加载状态
    private TextView headtxt;//头文件textview显示加载文字
    private TextView headtime;//头文件textview显示加载时间
    private ProgressBar progressBar;//加载进度


    public MyListViewUtils(Context context) {
        super(context);
        Init(context);
    }

    public MyListViewUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public MyListViewUtils(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        //拿到头布局文件xml
        headview=LinearLayout.inflate(context, R.layout.chat_head, null);
        headtxt=(TextView) headview.findViewById(R.id.headtxt);
        headtime=(TextView) headview.findViewById(R.id.timetxt);
        progressBar=(ProgressBar) headview.findViewById(R.id.headprogress);
        headtime.setText("上次更新时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        //拿到尾布局文件
        bottomview=LinearLayout.inflate(context, R.layout.chat_bottom, null);
        //测量尾文件高度
        bottomview.measure(0,0);
        //拿到高度
        bottomHeight=bottomview.getMeasuredHeight();
        //隐藏view
        bottomview.setPadding(0, 0, 0, 0);
        headview.measure(0, 0);
        headHeight=headview.getMeasuredHeight();
        headview.setPadding(0,-headHeight, 0, 0);
        //添加listview底部
        this.addFooterView(bottomview);
        //添加到listview头部
        this.addHeaderView(headview);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Yload=(int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY=(int) ev.getY();
                int paddingY=headHeight+(moveY-Yload)/2;
                if(paddingY<0){
                    headtxt.setText("下拉刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                if(paddingY>0){
                    headtxt.setText("松开刷新........");
                    progressBar.setVisibility(View.GONE);
                }
                headview.setPadding(0, paddingY, 0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(totaItemCounts==lassVisible&&scrollState==SCROLL_STATE_IDLE){
            if(!isLoading){
                isLoading=true;
                bottomview.setPadding(0, 0, 0, 400);
                //加载数据
                System.out.println("上拉加载");
                loadListener.onLoad();
            }
        }
//        Log.i("TGA", "firstVisible----"+firstVisible);
//        Log.i("TGA", "状态？"+(firstVisible==0));

        if(firstVisible==0&&scrollState==SCROLL_STATE_IDLE){
            headview.setPadding(0, 0, 0, 0);
            headtxt.setText("正在刷新.......");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.PullLoad();
        }
    }


    //接口回调
    public interface LoadListener{
        void onLoad();
        void PullLoad();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.firstVisible=firstVisibleItem;
        this.lassVisible=firstVisibleItem+visibleItemCount;
        this.totaItemCounts=totalItemCount;
//        System.out.println("firstVisible:"+firstVisible);
//        System.out.println("lassVisible"+lassVisible);
//        System.out.println("totaItemCounts"+totaItemCounts);
    }

    //加载完成
    public void loadComplete(){
        isLoading=false;
        bottomview.setPadding(0, 0, 0, 0);
        headview.setPadding(0, -headHeight, 0, 0);
    }

    public void setInteface(LoadListener loadListener){
        this.loadListener=loadListener;
    }

}