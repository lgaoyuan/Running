package com.csu.runningapplication.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.MyFetch;
import com.csu.runningapplication.jsonbean.MyJsonBean;

public class MyFragment extends Fragment {

    private WebView chartshow_wb;
    private MyApplication myApplication;

    TextView mBbsNum;
    TextView mFriendsNum;
    TextView mMileage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.my_fragment,parents,false);
        new FetchItemsTask().execute();//获取信息
        initEcharts(v);
        return v;
    }

    /**
     * 初始化echarts页面元素
     */
    private void initEcharts(View v){
        chartshow_wb=(WebView)v.findViewById(R.id.chartshow_wb);
        chartshow_wb.setBackgroundColor(0);
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

    /*
     * http请求
     * */
    private class FetchItemsTask extends AsyncTask<Void, Void, MyJsonBean>{
        MyJsonBean mj;
        @Override
        protected MyJsonBean doInBackground(Void... params) {
            mj=new MyFetch().fetchItems(getActivity(),myApplication.getUserid(),myApplication.getPassword(),"0","2019-11-29","2019-12-01");
            return mj;
        }

        @Override
        protected void onPostExecute(MyJsonBean result){// 执行完毕后，则更新UI

            //注册组件
            mBbsNum=(TextView)getActivity().findViewById(R.id.bbs_num);
            mFriendsNum=(TextView)getActivity().findViewById(R.id.friends_num);
            mMileage=(TextView)getActivity().findViewById(R.id.mileage);

            mBbsNum.setText(result.getBbsnum());
            mFriendsNum.setText(result.getFriends());
            mMileage.setText(Double.toString(result.getCycling()+result.getRunning()));
        }
    }
}
