package com.csu.runningapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.csu.runningapplication.http.LoginFetch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends Activity {
    private MyApplication myApplication;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String account;
    private String name;
    private String password;
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        myApplication = (MyApplication) getApplication();

        requestPermissionsIfAboveM();
    }

    /*开始申请权限*/
    private Map<String, String> permissionHintMap = new HashMap<>();
    private void requestPermissionsIfAboveM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Map<String, String> requiredPermissions = new HashMap<>();
            requiredPermissions.put(Manifest.permission.ACCESS_FINE_LOCATION, "定位");
            requiredPermissions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
            requiredPermissions.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
            requiredPermissions.put(Manifest.permission.CAMERA, "相机");
            requiredPermissions.put(Manifest.permission.READ_PHONE_STATE, "读取设备信息");
            for (String permission : requiredPermissions.keySet()) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionHintMap.put(permission, requiredPermissions.get(permission));
                }
            }
            if (!permissionHintMap.isEmpty()) {
                requestPermissions(permissionHintMap.keySet().toArray(new String[0]), REQUEST_CODE_PERMISSION);
            }else{
                startMain();//开始
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> failPermissions = new LinkedList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                failPermissions.add(permissions[i]);
            }
        }
        if (!failPermissions.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String permission : failPermissions) {
                sb.append(permissionHintMap.get(permission)).append("、");
            }
            sb.deleteCharAt(sb.length() - 1);
            String hint = "未授予必要权限: " +
                    sb.toString() +
                    "，请前往设置页面开启权限";
            new AlertDialog.Builder(this)
                    .setMessage(hint)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    System.exit(0);
                }
            }).show();
        }else{
            startMain();//开始
        }
    }

    private void startMain(){
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        account = pref.getString("userid", null);
        name = pref.getString("name", null);
        password = pref.getString("password", null);
        if(account!=null&&name!=null&&password!=null){
            myApplication.setUserid(account);
            myApplication.setName(name);
            myApplication.setPassword(password);
            new LoginItemsTask().execute();
        }else{
            Intent i=new Intent(HomePageActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    /*
     * http登录
     * */
    private class LoginItemsTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            return new LoginFetch().fetchItems(account,name,password);
        }

        @Override
        protected void onPostExecute(Integer result) {// 执行完毕
            if (result == 1) {
                myApplication.setUserid(account);
                myApplication.setName(name);
                myApplication.setPassword(password);

                Intent i=new Intent(HomePageActivity.this,MainActivity.class);
                startActivity(i);
                HomePageActivity.this.finish();
            }else{
                Intent i=new Intent(HomePageActivity.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }
}
