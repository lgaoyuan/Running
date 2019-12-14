package com.csu.runningapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csu.runningapplication.http.MyFetch;
import com.csu.runningapplication.http.NewBbsFetch;
import com.csu.runningapplication.jsonbean.IdJsonBean;
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

public class Chat_dynamicActivity extends Activity {

    private TextView mUpload;
    private TextView mBackpress;
    private EditText mEditText;
    private ImageView mChooseImg;
    private LinearLayout mImgLayout;
    private List<String> imgPath=new ArrayList<>();
    private MyApplication myApplication;

    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_dynamic);
        myApplication = (MyApplication) getApplication();
        mEditText=(EditText)findViewById(R.id.edittext);
        mImgLayout=(LinearLayout)findViewById(R.id.imgLayout);
        mBackpress=(TextView)findViewById(R.id.backpress);
        mBackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mUpload=(TextView)findViewById(R.id.upload_bbs);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=mEditText.getText().toString();
                new BbsTask().execute();
            }
        });

        mChooseImg=(ImageView)findViewById(R.id.chooseImg);
        mChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });
    }

    /*
    * 选取图片
    * */
    private void chooseImg(){
        Matisse.from(this).choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(9)
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
                .gridExpectedSize(mChooseImg.getWidth())
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

                int len=Matisse.obtainPathResult(data).size();
                for(int i=0;i<len;i++) {
                    String path = Matisse.obtainPathResult(data).get(i);
                    if(imgPath.size()>=9){
                        Toast.makeText(this, "最多选取9张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imgPath.add(path);
                    if (path != null) {
                        ImageView mNewImg=new ImageView(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mChooseImg.getWidth(),mChooseImg.getHeight());
                        params.setMargins(10,10,10,10);
                        mNewImg.setLayoutParams(params);
                        mNewImg.setBackgroundColor(getResources().getColor(R.color.colorImgBackground));
                        mNewImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                AlertDialog alertDialog = new AlertDialog.Builder(Chat_dynamicActivity.this)
                                        .setMessage("是否删除这张图片")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //得到view在父容器中的位置下标
                                                int index=((ViewGroup)view.getParent()).indexOfChild(view);
                                                imgPath.remove(index);
                                                mImgLayout.removeView(view);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        })
                                        .create();
                                alertDialog.show();
                            }
                        });
                        mImgLayout.addView(mNewImg);
                        Glide.with(this)
                                .asBitmap() // some .jpeg files are actually gif
                                .load(path)
                                .thumbnail(0.67f)//压缩
                                .apply(new RequestOptions() {{
                                    override(Target.SIZE_ORIGINAL);
                                }})
                                .into(mNewImg);
                    } else
                        Toast.makeText(this, "选取图片失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    /*
    * 上传帖子
    * */
    private class BbsTask extends AsyncTask<Void, Void, IdJsonBean> {

        @Override
        protected IdJsonBean doInBackground(Void... params) {
            return new NewBbsFetch().fetchItems(myApplication.getUserid(),content);
        }

        @Override
        protected void onPostExecute(IdJsonBean result) {// 执行完毕
            if (result == null) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            for(String str:imgPath){
                uploadFile(result.getId(),str);
            }
            Toast.makeText(Chat_dynamicActivity.this,"发布成功！",Toast.LENGTH_SHORT).show();
            Chat_dynamicActivity.this.finish();
        }
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */
    /*private String compressImage(String filePath) {

        //原文件
        File oldFile = new File(filePath);

        int quality = 70;//压缩比例0-100
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片

        String lastName=oldFile.getName().substring(oldFile.getName().lastIndexOf("."));
        File externalFilesDir = getExternalFilesDir("CompressImage");
        File outputFile=new File(externalFilesDir+"/1"+lastName);//压缩图
        Log.d("outputFile:",externalFilesDir+"");
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
    }*/

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    /*public static Bitmap getSmallBitmap(String filePath) {
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
    }*/

    // 使用OkHttp上传文件
    private void uploadFile(String id,String path) {
        //path=compressImage(path);
        File file=new File(path);
        OkHttpClient client = new OkHttpClient();
        MediaType contentType = MediaType.parse("text/plain"); // 上传文件的Content-Type
        RequestBody fileBody = RequestBody.create(contentType, file); // 上传文件的请求体
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id", id).addFormDataPart("file", path, fileBody).build();
        Request request = new Request.Builder()
                .url("https://lgaoyuan.xyz:8080/running/upload") // 上传地址
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
                    Log.i("onResponse", "uploadFileSuccess: " + response.body().string());
                } else {
                    Log.i("onResponse", "uploadFileError: " + response.message());
                }
            }
        });
    }

}
