package com.csu.runningapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csu.runningapplication.http.ContentFetch;
import com.csu.runningapplication.util.GlideLoadEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
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
    private LinearLayout mImg;
    private LinearLayout mPassword;
    private Button mQuit;
    private EditText mEditContent;
    private Button mContentOk;
    private ImageView mMyImg;
    private Button mImgOk;
    private String content;
    private String imgPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        setContentView(R.layout.setting_layout);
        mPassword = (LinearLayout) findViewById(R.id.change_password);
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetActivity.this, PasswordActivity.class);
                startActivity(i);
            }
        });

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
}
