package com.csu.runningapplication.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.http.EchartsFetch;
import com.csu.runningapplication.http.MyFetch;
import com.csu.runningapplication.jsonbean.MyJsonBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyFragment extends Fragment {

    private WebView chartshow_wb;
    private MyApplication myApplication;
    View v;

    private TextView mDate;
    private TextView mBbsNum;
    private TextView mFriendsNum;
    private TextView mMileage;
    private TextView mTime;
    private TextView mSpeed;
    private TextView mWeek;
    private TextView mMonth;
    private TextView mYear;
    private String runData;

    private String x = null;//横轴

    private String startDate;
    private String endDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.my_fragment, parents, false);
        new MyItemsTask().execute();
        x = "['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']";
        new EchartsItemsTask().execute("0");//获取数据
        getDay(0);
        //初始化echarts
        mDate = (TextView) v.findViewById(R.id.my_data_date);
        mDate.setText(startDate + "~" + endDate);

        chartshow_wb = (WebView) v.findViewById(R.id.chartshow_wb);
        chartshow_wb.setBackgroundColor(0);
        //进行webwiev的一堆设置
        //开启本地文件读取（默认为true，不设置也可以）
        chartshow_wb.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        chartshow_wb.getSettings().setJavaScriptEnabled(true);
        chartshow_wb.loadUrl("file:///android_asset/echarts.html");

        chartshow_wb.reload();//放在第一次加载不出来?


        mWeek = v.findViewById(R.id.my_week);
        mMonth = v.findViewById(R.id.my_month);
        mYear = v.findViewById(R.id.my_year);
        mWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDay(0);
                mDate.setText(startDate + "~" + endDate);
                x = "['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']";
                new EchartsItemsTask().execute("0");
                mWeek.setBackgroundResource(R.drawable.back_circle);
                mMonth.setBackgroundResource(R.color.colorTrans);
                mYear.setBackgroundResource(R.color.colorTrans);
                chartshow_wb.reload();
            }
        });
        mMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDay(1);
                mDate.setText(startDate + "~" + endDate);
                new EchartsItemsTask().execute("0");
                mWeek.setBackgroundResource(R.color.colorTrans);
                mMonth.setBackgroundResource(R.drawable.back_circle);
                mYear.setBackgroundResource(R.color.colorTrans);
                chartshow_wb.reload();
            }
        });
        mYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDay(2);
                mDate.setText(startDate + "~" + endDate);
                x = "['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']";
                new EchartsItemsTask().execute("1");
                mWeek.setBackgroundResource(R.color.colorTrans);
                mMonth.setBackgroundResource(R.color.colorTrans);
                mYear.setBackgroundResource(R.drawable.back_circle);
                chartshow_wb.reload();
            }
        });
        return v;
    }

    /**
     * 加载echarts数据
     */
    private void initEcharts() {
        chartshow_wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                chartshow_wb.loadUrl("javascript:createBarLineChart(" + x + "," + runData + ");");
            }
        });
    }

    /*
     * http请求我的信息
     * */
    private class MyItemsTask extends AsyncTask<Void, Void, MyJsonBean> {
        MyJsonBean mj;

        @Override
        protected MyJsonBean doInBackground(Void... params) {
            mj = new MyFetch().fetchItems(myApplication.getUserid(), myApplication.getPassword());
            return mj;
        }

        @Override
        protected void onPostExecute(MyJsonBean result) {// 执行完毕后，则更新UI
            if (result == null) {
                Toast.makeText(getActivity().getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            //注册组件
            mBbsNum = (TextView) getActivity().findViewById(R.id.bbs_num);
            mFriendsNum = (TextView) getActivity().findViewById(R.id.friends_num);
            mMileage = (TextView) getActivity().findViewById(R.id.mileage);
            mTime = (TextView) getActivity().findViewById(R.id.my_time);
            mSpeed = (TextView) getActivity().findViewById(R.id.my_speed);

            mBbsNum.setText(Integer.toString(result.getBbsnum()));
            mFriendsNum.setText(Integer.toString(result.getFriends()));
            mMileage.setText(Double.toString((result.getCycling() + result.getRunning()) / 1000));
            int second = result.getTime();
            int min = second / 60;
            int hour = second / 3600;
            second = second % 60;
            DecimalFormat df = new DecimalFormat("00");
            mTime.setText(df.format(hour) + ":" + df.format(min) + ":" + df.format(second));
            if (result.getCycling() + result.getRunning() != 0) {
                int speed = (int) (result.getTime() / ((result.getCycling() + result.getRunning()) / 1000));
                min = speed / 60;
                speed = speed % 60;
                mSpeed.setText(min + "'" + df.format(speed) + "''");
            }
        }
    }

    /*
     * 获取第一天和最后一天日期
     * */
    private void getDay(int type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            switch (type) {
                case 0://周
                    cal.set(Calendar.DAY_OF_WEEK, 1);
                    startDate = format.format(cal.getTime());
                    cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 6);
                    endDate = format.format(cal.getTime());
                    break;

                case 1://月
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    startDate = format.format(cal.getTime());
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, 0);
                    endDate = format.format(cal.getTime());
                    break;

                case 2://年
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    startDate = format.format(cal.getTime());
                    cal.add(Calendar.YEAR, 1);
                    cal.set(Calendar.DAY_OF_YEAR, 0);
                    endDate = format.format(cal.getTime());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * http请求echarts信息
     * */
    private class EchartsItemsTask extends AsyncTask<String, Void, String> {
        String str;

        @Override
        protected String doInBackground(String... params) {
            str = new EchartsFetch().fetchItems(myApplication.getUserid(), params[0], startDate, endDate);
            return str;
        }

        @Override
        protected void onPostExecute(String result) {// 执行完毕后，则更新UI
            if (result == null) {
                return;
            }
            runData = result;
            initEcharts();
        }
    }
}
