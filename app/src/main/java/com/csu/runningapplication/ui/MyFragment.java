package com.csu.runningapplication.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csu.runningapplication.CircleImageView;
import com.csu.runningapplication.FriendsActivity;
import com.csu.runningapplication.Friends_list;
import com.csu.runningapplication.JoinActivity;
import com.csu.runningapplication.LoginActivity;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.SetActivity;
import com.csu.runningapplication.http.EchartsFetch;
import com.csu.runningapplication.http.MyFetch;
import com.csu.runningapplication.http.MyRank;
import com.csu.runningapplication.jsonbean.MyJsonBean;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment {


    private WebView chartshow_wb;
    private MyApplication myApplication;
    View v;

    private TextView mUserName;
    private TextView mContent;
    private TextView mDate;
    private TextView mBbsNum;
    private TextView mFriendsNum;
    private TextView mActNum;
    private TextView mTag;
    private TextView mMileage;
    private TextView mTime;
    private TextView mSpeed;
    private TextView mCalorie;
    private TextView mWeek;
    private TextView mMonth;
    private TextView mYear;
    private String runData;
    private LinearLayout lin;
    private LinearLayout act;
    private LinearLayout mSetting;

    private CircleImageView img;
    private String x = null;//横轴

    private Calendar startCal = Calendar.getInstance();
    private Calendar endCal = Calendar.getInstance();
    private Date startDate;
    private Date endDate;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private Button add;
    private TextView myrank1;
    private TextView myrank2;
    private TextView myrank3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.my_fragment, parents, false);

        return init();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();

    }

    private View init() {
        mUserName = (TextView) v.findViewById(R.id.my_user_name);
        mUserName.setText(myApplication.getName());
        img = v.findViewById(R.id.my_user_img);
        myrank1=v.findViewById(R.id.my_rank1);
        myrank2=v.findViewById(R.id.my_rank2);
        myrank3=v.findViewById(R.id.my_rank3);

        new MyItemsTask().execute();

        x = "['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']";
        new EchartsItemsTask().execute("0");//获取数据
        getDay(0);
        new GetMyRankTask().execute();
        //初始化add添加好友按钮
        add = (Button) v.findViewById(R.id.add_friends);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FriendsActivity.class);
                startActivity(i);
            }
        });
        lin = (LinearLayout) v.findViewById(R.id.friends_list);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Friends_list.class);
                startActivity(i);
            }
        });
        act = (LinearLayout) v.findViewById(R.id.act_list);
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), JoinActivity.class);
                startActivity(i);

            }
        });

        //初始化echarts
        mDate = (TextView) v.findViewById(R.id.my_data_date);
        setMDate();

        chartshow_wb = (WebView) v.findViewById(R.id.chartshow_wb);
        chartshow_wb.setBackgroundColor(0);
        //进行webwiev的一堆设置
        //开启本地文件读取（默认为true，不设置也可以）
        chartshow_wb.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        chartshow_wb.getSettings().setJavaScriptEnabled(true);
        chartshow_wb.loadUrl("file:///android_asset/echarts.html");

        chartshow_wb.reload();//放在第一次加载不出来?

        mSetting = (LinearLayout) v.findViewById(R.id.setting);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SetActivity.class);
                i.putExtra("content", mContent.getText());
                startActivity(i);
            }
        });

        Button mSetButton=(Button)v.findViewById(R.id.ic_setting);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SetActivity.class);
                i.putExtra("content", mContent.getText());
                startActivity(i);
            }
        });


        mWeek = v.findViewById(R.id.my_week);
        mMonth = v.findViewById(R.id.my_month);
        mYear = v.findViewById(R.id.my_year);
        mWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDay(0);
                new GetMyRankTask().execute();
                setMDate();
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
                new GetMyRankTask().execute();
                setMDate();
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
                new GetMyRankTask().execute();
                setMDate();
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
            mContent = (TextView) getActivity().findViewById(R.id.my_content);
            mBbsNum = (TextView) getActivity().findViewById(R.id.bbs_num);
            mFriendsNum = (TextView) getActivity().findViewById(R.id.friends_num);
            mActNum = (TextView) getActivity().findViewById(R.id.act_num);
            mTag=(TextView)getActivity().findViewById(R.id.my_tag);
            mMileage = (TextView) getActivity().findViewById(R.id.mileage);
            mTime = (TextView) getActivity().findViewById(R.id.my_time);
            mSpeed = (TextView) getActivity().findViewById(R.id.my_speed);
            mCalorie = (TextView) getActivity().findViewById(R.id.my_calorie);


            String contentStr = result.getContent();
            if (contentStr == null) {
                contentStr = "这个人很懒，什么都没有写";
            }
            mContent.setText(contentStr);
            mBbsNum.setText(Integer.toString(result.getBbsnum()));
            mFriendsNum.setText(Integer.toString(result.getFriends()));
            mActNum.setText(Integer.toString(result.getActnum()));
            if(result.getTag().equals("")){
                mTag.setVisibility(View.GONE);
            }else{
                mTag.setText(result.getTag());
            }

            mMileage.setText(Double.toString((result.getCycling() + result.getRunning()) / 1000));
            mCalorie.setText(Integer.toString(result.getCalorie()));
            RequestOptions requestOptions=new RequestOptions().error(R.drawable.user_192);
            Glide.with(getContext())
                    .load(result.getAvatarUrl())
                    .apply(requestOptions)
                    .into(img);
            int second = result.getTime();
            int min = second / 60;
            int hour = second / 3600;
            second = second % 60;
            DecimalFormat df = new DecimalFormat("00");
            mTime.setText(df.format(hour) + ":" + df.format(min) + ":" + df.format(second));
            if (result.getCycling() + result.getRunning() != 0) {
                int speed = (int) (result.getTime() / (((result.getCycling()/2) + result.getRunning()) / 1000));
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
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            switch (type) {
                case 0://周
                    cal.set(Calendar.DAY_OF_WEEK, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 0);//0点
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    startDate = cal.getTime();
                    cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 6);
                    cal.set(Calendar.HOUR_OF_DAY, 23);//最后1秒
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    endDate = cal.getTime();
                    break;

                case 1://月
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 0);//0点
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    startDate = cal.getTime();
                    cal.add(Calendar.MONTH, 1);
                    cal.set(Calendar.DAY_OF_MONTH, 0);
                    cal.set(Calendar.HOUR_OF_DAY, 23);//最后1秒
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    endDate = cal.getTime();
                    break;

                case 2://年
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 0);//0点
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    startDate = cal.getTime();
                    cal.add(Calendar.YEAR, 1);
                    cal.set(Calendar.DAY_OF_YEAR, 0);
                    cal.set(Calendar.HOUR_OF_DAY, 23);//最后1秒
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    endDate = cal.getTime();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 设置mDate TextView文字
     * */
    private void setMDate() {
        startCal.setTime(startDate);
        endCal.setTime(endDate);
        mDate.setText(startCal.get(Calendar.YEAR) + "年" + (startCal.get(Calendar.MONTH) + 1) + "月" + startCal.get(Calendar.DATE) + "日" + "-" + endCal.get(Calendar.YEAR) + "年" + (endCal.get(Calendar.MONTH) + 1) + "月" + endCal.get(Calendar.DATE) + "日");
    }


    /*
     * http请求echarts信息
     * */
    private class EchartsItemsTask extends AsyncTask<String, Void, String> {
        String str;

        @Override
        protected String doInBackground(String... params) {
            str = new EchartsFetch().fetchItems(myApplication.getUserid(), params[0], String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()));
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

    private class GetMyRankTask extends AsyncTask<Void,Void,String>{
        String mj;
        @Override
        protected String doInBackground(Void... voids) {
            mj=new MyRank().fetchItems(myApplication.getUserid(),String.valueOf(startDate.getTime()),String.valueOf(endDate.getTime()));
            return mj;
        }
        @Override
        protected void onPostExecute(String result){
            if (result == null) {
                return;
            }
            try {

                    JSONObject jb=new JSONObject(result);
                  myrank1.setText(jb.getString("r2"));
                  myrank2.setText(jb.getString("r0"));
                  myrank3.setText(jb.getString("r1"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
