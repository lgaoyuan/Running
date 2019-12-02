package com.csu.runningapplication.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.csu.runningapplication.MyApplication;
import com.csu.runningapplication.R;
import com.csu.runningapplication.RunningActivity;
import com.csu.runningapplication.http.EchartsFetch;
import com.csu.runningapplication.http.MileageFetch;

public class RunFragment extends Fragment {

    private MyApplication myApplication;
    private TextView mTextView;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=(MyApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parents, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.run_fragment, parents, false);
        new RunItemsTask().execute(0);//获取数据
        final Button mRun=(Button)v.findViewById(R.id.select_run);
        final Button mCycle=(Button)v.findViewById(R.id.select_cycle);
        mRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCycle.setBackground(getResources().getDrawable(R.drawable.ic_cycle_normal));
                mRun.setBackground(getResources().getDrawable(R.drawable.ic_run_select));
                new RunItemsTask().execute(0);//获取数据
            }
        });
        mCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCycle.setBackground(getResources().getDrawable(R.drawable.ic_cycle_select));
                mRun.setBackground(getResources().getDrawable(R.drawable.ic_run_normal));
                new RunItemsTask().execute(1);//获取数据
            }
        });


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

    /*
     * http请求信息
     * */
    private class RunItemsTask extends AsyncTask<Integer, Void, Double> {
        @Override
        protected Double doInBackground(Integer... params) {
            myApplication.setType(params[0]);
            double resule=new MileageFetch().fetchItems(myApplication.getUserid(),myApplication.getType());
            return resule;
        }

        @Override
        protected void onPostExecute(Double result){// 执行完毕后，则更新UI
            if(result==-1){
                Toast.makeText(getActivity().getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                return;
            }
            mTextView=(TextView)v.findViewById(R.id.mileage);
            mTextView.setText(String.format("%.2f", result / 1000));
        }
    }
}
