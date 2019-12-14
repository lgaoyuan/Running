package com.csu.runningapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.csu.runningapplication.http.LoginFetch;

public class LoginActivity extends Activity {
    private MyApplication myApplication;
    private EditText mAccount;
    private EditText mPassword;
    private EditText mName;
    private ImageView mLogin;

    private String account;
    private String password;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication = (MyApplication) getApplication();
        mAccount=(EditText)findViewById(R.id.account);
        mName=(EditText)findViewById(R.id.name);
        mPassword=(EditText)findViewById(R.id.password);
        mLogin=(ImageView)findViewById(R.id.loginButton);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account=mAccount.getText().toString();
                name=mName.getText().toString();
                password=mPassword.getText().toString();
                new LoginItemsTask().execute();
            }
        });
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
        protected void onPostExecute(Integer result) {// 执行完毕后，则更新UI
            if (result == 1) {
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();//持久化
                editor.putString("userid", account);
                editor.putString("name", name);
                editor.putString("password", password);
                editor.commit();

                myApplication.setUserid(account);
                myApplication.setName(name);
                myApplication.setPassword(password);

                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }else if(result==0){
                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
