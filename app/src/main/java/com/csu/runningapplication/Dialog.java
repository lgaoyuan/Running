package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.csu.runningapplication.ui.Chat_dialog;

import java.util.ArrayList;
import java.util.List;

public class Dialog extends AppCompatActivity {
    private MyApplication bpp;
    private ImageView image;
    private String test;
    private String headimg;
    private ViewPager vp;
    private Chat_dialog oneFragment;
    private Chat_dialog twoFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private ArrayList<String> infoList=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        Intent intent=getIntent();

        test=getIntent().getStringExtra("test");
        headimg=getIntent().getStringExtra("headimg");
        infoList=getIntent().getStringArrayListExtra("imglist");
        //判断点击的是头像还是说说图片
        if(test.equals("0")){
            initViewforhead();
            vp.setOffscreenPageLimit(1);//ViewPager的缓存为list的长度。
        }else{
            initViews();
            vp.setOffscreenPageLimit(infoList.size());//ViewPager的缓存为list的长度。
        }



        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        //缓存应设置最大值

        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧


        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }
    private void initViews() {

        vp = (ViewPager) findViewById(R.id.mainViewPager);

        for(int i=0;i<infoList.size();i++){
            Chat_dialog fragment=new Chat_dialog(infoList.get(i));
            mFragmentList.add(fragment);
        }
//        //此处构造函数后期改为json获取的图片
//        oneFragment = new Chat_dialog(R.drawable.chat_img);
//        twoFragment = new Chat_dialog(R.drawable.user_192);
//        //给FragmentList添加数据
//        mFragmentList.add(oneFragment);
//        mFragmentList.add(twoFragment);
    }
    private void initViewforhead(){
        vp = (ViewPager) findViewById(R.id.mainViewPager);
        Chat_dialog fragment1=new Chat_dialog(headimg);
        System.out.println(headimg+"123");
        mFragmentList.add(fragment1);

    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();

    }
    }

