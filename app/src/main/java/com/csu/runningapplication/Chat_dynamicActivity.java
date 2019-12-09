package com.csu.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csu.runningapplication.util.GlideLoadEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chat_dynamicActivity extends Activity {
    private TextView mBackpress;
    private ImageView mChooseImg;
    private List<String> imgPath=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_dynamic);
        mBackpress=(TextView)findViewById(R.id.backpress);
        mBackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                .gridExpectedSize((int) getResources().getDimension(R.dimen.imageSelectDimen))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .theme(R.style.Matisse_Dracula)//黑色主题
                .thumbnailScale(0.87f)//压缩
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
                    imgPath.add(path);
                    if (path != null) {
                        Glide.with(this)
                                .asBitmap() // some .jpeg files are actually gif
                                .load(path)
                                .apply(new RequestOptions() {{
                                    override(Target.SIZE_ORIGINAL);
                                }})
                                .into(mChooseImg);
                    } else
                        Toast.makeText(this, "选取图片失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
