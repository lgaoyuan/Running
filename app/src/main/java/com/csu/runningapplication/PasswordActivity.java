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
import com.csu.runningapplication.http.PasFetch;

public class PasswordActivity extends Activity {
    private MyApplication myApplication;
    private EditText mOld;
    private EditText mNew;
    private EditText mAgain;
    private ImageView mOk;

    private String oldPas;
    private String newPas;
    private String againPas;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        myApplication = (MyApplication) getApplication();
        mOld=(EditText)findViewById(R.id.old_pas);
        mNew=(EditText)findViewById(R.id.new_pas);
        mAgain=(EditText)findViewById(R.id.again_pas);
        
        mOk=(ImageView)findViewById(R.id.okButton);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPas=mOld.getText().toString();
                againPas=mAgain.getText().toString();
                newPas=mNew.getText().toString();
                if(newPas.equals(againPas)){
                    new OkItemsTask().execute();
                }else{
                    Toast.makeText(PasswordActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /*
     * http登录
     * */
    private class OkItemsTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            return new PasFetch().fetchItems(myApplication.getUserid(),oldPas,newPas);
        }

        @Override
        protected void onPostExecute(Integer result) {// 执行完毕
            if (result == 1) {
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();//持久化
                editor.putString("userid", null);
                editor.putString("againPas", null);
                editor.putString("newPas", null);
                editor.commit();
                myApplication.setUserid(null);
                myApplication.setName(null);
                myApplication.setPassword(null);
                Toast.makeText(PasswordActivity.this,"修改成功，请重新登录！",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(PasswordActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                PasswordActivity.this.finish();
            }else if(result==0){
                Toast.makeText(PasswordActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PasswordActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
