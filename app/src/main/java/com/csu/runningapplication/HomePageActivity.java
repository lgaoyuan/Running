package com.csu.runningapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends Activity {

    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        requestPermissionsIfAboveM();
    }

    /*开始申请权限*/
    private Map<String, String> permissionHintMap = new HashMap<>();
    private void requestPermissionsIfAboveM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Map<String, String> requiredPermissions = new HashMap<>();
            requiredPermissions.put(Manifest.permission.ACCESS_FINE_LOCATION, "定位");
            requiredPermissions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(HomePageActivity.this,MainActivity.class);
                startActivity(i);
                HomePageActivity.this.finish();
            }
        },2000);
    }
}
