package com.csu.runningapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csu.runningapplication.http.ContentFetch;
import com.csu.runningapplication.http.NickFetch;
import com.csu.runningapplication.http.SetTagFetch;
import com.csu.runningapplication.http.TagsFetch;
import com.csu.runningapplication.util.GlideLoadEngine;
import com.csu.runningapplication.views.PickerScrollView;
import com.csu.runningapplication.views.Pickers;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetActivity extends Activity {
    private MyApplication myApplication;
    private LinearLayout mContent;
    private LinearLayout mNick;
    private LinearLayout mImg;
    private LinearLayout mPassword;
    private Button mQuit;
    private EditText mEditContent;
    private EditText mEditNick;
    private Button mContentOk;
    private Button mNickOk;
    private ImageView mMyImg;
    private Button mImgOk;
    private String content;
    private String nick;
    private String imgPath;
    private LinearLayout bt_scrollchoose; // 滚动选择器按钮
    private PickerScrollView pickerscrlllview; // 滚动选择器
    private List<Pickers> list; // 滚动选择器数据
    private String[] id;
    private String[] name;

    private Button bt_yes; // 确定按钮
    private Button bt_no; // 取消按钮
    private RelativeLayout picker_rel; // 选择器布局
    private String chooseTag=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        setContentView(R.layout.setting_layout);
        initView();
        new TagsTask().execute();
    }



    /**
     * 初始化
     */
    private void initView() {
        mPassword = (LinearLayout) findViewById(R.id.change_password);
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetActivity.this, PasswordActivity.class);
                startActivity(i);
            }
        });

        mNick = (LinearLayout) findViewById(R.id.change_nick);
        mEditNick = (EditText) findViewById(R.id.new_nick);

        mContent = (LinearLayout) findViewById(R.id.change_text);
        mEditContent = (EditText) findViewById(R.id.new_content);
        Intent i = getIntent();
        content = i.getStringExtra("content");
        mContentOk = (Button) findViewById(R.id.content_ok);
        mContentOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=mEditContent.getText().toString();
                new ContentItemsTask().execute();
            }
        });
        nick = i.getStringExtra("nick");
        mNickOk = (Button) findViewById(R.id.nick_ok);
        mNickOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nick=mEditNick.getText().toString();
                new NickItemsTask().execute();
            }
        });

        mImgOk = (Button) findViewById(R.id.img_ok);
        mImgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgPath!=null){
                    uploadFile(myApplication.getUserid(),imgPath);
                    Toast.makeText(SetActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    mMyImg.setVisibility(View.GONE);
                    mImgOk.setVisibility(View.GONE);
                }
            }
        });
        mMyImg = (ImageView) findViewById(R.id.chooseMyImg);
        mMyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });

        mNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEditNick.setText(nick);
                mEditNick.setVisibility(View.VISIBLE);
                mNickOk.setVisibility(View.VISIBLE);
            }
        });

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEditContent.setText(content);
                mEditContent.setVisibility(View.VISIBLE);
                mContentOk.setVisibility(View.VISIBLE);
            }
        });

        mImg = (LinearLayout) findViewById(R.id.change_img);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyImg.setVisibility(View.VISIBLE);
                mImgOk.setVisibility(View.VISIBLE);
            }
        });

        mQuit = (Button) findViewById(R.id.quit);
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();//持久化
                editor.putString("userid", null);
                editor.putString("name", null);
                editor.putString("password", null);
                editor.commit();
                myApplication.setUserid(null);
                myApplication.setName(null);
                myApplication.setPassword(null);
                Intent i = new Intent(SetActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        //标签滚动选择器
        bt_scrollchoose = (LinearLayout) findViewById(R.id.change_tag);
        bt_scrollchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_rel.setVisibility(View.VISIBLE);
            }
        });
        picker_rel = (RelativeLayout) findViewById(R.id.picker_rel);
        pickerscrlllview = (PickerScrollView) findViewById(R.id.pickerscrlllview);
        pickerscrlllview.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                //System.out.println("选择：" + pickers.getShowId() + pickers.getShowConetnt());
                chooseTag=pickers.getShowConetnt();
            }
        });
        bt_yes = (Button) findViewById(R.id.picker_yes);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetTagTask().execute();
                picker_rel.setVisibility(View.GONE);
            }
        });
        bt_no = (Button) findViewById(R.id.picker_no);
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_rel.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化标签选择数据
     */
    private void initData(String[] idArr,String[] nameArr) {
        list = new ArrayList<Pickers>();
        id = idArr;
        name = nameArr;
        chooseTag=name[0];
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], id[i]));
        }
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);
    }




    private class ContentItemsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new ContentFetch().fetchItems(myApplication.getUserid(), content);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(SetActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            mEditContent.setVisibility(View.GONE);
            mContentOk.setVisibility(View.GONE);
        }
    }

    private class NickItemsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new NickFetch().fetchItems(myApplication.getUserid(), nick);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(SetActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            mEditNick.setVisibility(View.GONE);
            mNickOk.setVisibility(View.GONE);
        }
    }

    /*
     * 选取图片
     * */
    private void chooseImg() {
        Matisse.from(this).choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(1)
                .addFilter(new Filter() {
                    @Override
                    protected Set<MimeType> constraintTypes() {
                        return new HashSet<MimeType>() {{
                            add(MimeType.PNG);
                        }};
                    }

                    @Override
                    public IncapableCause filter(Context context, Item item) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(item.getContentUri());
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream(inputStream, null, options);
                            /*int width = options.outWidth;
                            int height = options.outHeight;

                            if (width >= 500)
                                return new IncapableCause("宽度超过500px");*/

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        return null;
                    }
                })
                .gridExpectedSize(mMyImg.getWidth())
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .theme(R.style.Matisse_Dracula)//黑色主题
                .thumbnailScale(0.67f)//压缩
                .imageEngine(new GlideLoadEngine())
                .forResult(1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                imgPath = Matisse.obtainPathResult(data).get(0);
                if (imgPath != null) {
                    Glide.with(this)
                            .asBitmap() // some .jpeg files are actually gif
                            .load(imgPath)
                            .thumbnail(0.67f)//压缩
                            .apply(new RequestOptions() {{
                                override(Target.SIZE_ORIGINAL);
                            }})
                            .into(mMyImg);
                } else
                    Toast.makeText(this, "选取图片失败", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */
    private String compressImage(String filePath) {

        //原文件
        File oldFile = new File(filePath);

        int quality = 50;//压缩比例0-100
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片

        String lastName=oldFile.getName().substring(oldFile.getName().lastIndexOf("."));
        File externalFilesDir = getExternalFilesDir("CompressImage");
        File outputFile=new File(externalFilesDir+"/1"+lastName);//压缩图
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 使用OkHttp上传文件
    private void uploadFile(String id,String path) {
        path=compressImage(path);
        File file=new File(path);
        OkHttpClient client = new OkHttpClient();
        MediaType contentType = MediaType.parse("text/plain"); // 上传文件的Content-Type
        RequestBody fileBody = RequestBody.create(contentType, file); // 上传文件的请求体
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("userid", id).addFormDataPart("file", path, fileBody).build();
        Request request = new Request.Builder()
                .url("https://lgaoyuan.xyz:8080/running/changeImg") // 上传地址
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 文件上传失败
                Log.i("onFailure", "uploadFileFail: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 文件上传成功
                if (response.isSuccessful()) {

                } else {
                    Log.i("onResponse", "uploadFileError: " + response.message());
                }
            }
        });
    }

    /*
    * 标签列表
    * */
    private class TagsTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String tj=new TagsFetch().fetchItems();
            return tj;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray json=new JSONArray(result);
                ArrayList<String> idList=new ArrayList<String>();
                ArrayList<String> nameList=new ArrayList<String>();
                for(int i=0;i<json.length();i++){
                    JSONObject jb=json.getJSONObject(i);
                    idList.add(jb.getString("id"));
                    nameList.add(jb.getString("tag"));
                }
                String[] id = new String[idList.size()];
                String[] name = new String[nameList.size()];
                idList.toArray(id);
                nameList.toArray(name);
                initData(id,name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * 设置标签
    * */
    private class SetTagTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new SetTagFetch().fetchItems(chooseTag,myApplication.getUserid());
            return null;
        }
    }

}
