package com.csu.runningapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.csu.runningapplication.R;

public class MyFragment extends Fragment {

    private WebView chartshow_wb;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.my_fragment,parents,false);
        initEcharts(v);
        return v;
    }

    /**
     * 初始化页面元素
     */
    private void initEcharts(View v){
        chartshow_wb=(WebView)v.findViewById(R.id.chartshow_wb);
        //进行webwiev的一堆设置
        //开启本地文件读取（默认为true，不设置也可以）
        chartshow_wb.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        chartshow_wb.getSettings().setJavaScriptEnabled(true);
        chartshow_wb.loadUrl("file:///android_asset/echarts.html");

        chartshow_wb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                chartshow_wb.loadUrl("javascript:createBarLineChart();");
            }
        });
    }
}
