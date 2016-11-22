package com.zeroami.youliao.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeroami.commonlib.base.LBaseActivity;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：大图页</p>
 */
public class ImageActivity extends LBaseActivity {

    private static final String EXTRA_IMAGE_URL = "extra_image_url";
//    private static final String SHARE_TRANSITION_NAME = LRUtils.getString(R.string.transition_share_image);

    @Bind(R.id.cv_image)
    ImageView cvImage;

    private String mImageUrl;

    public static void launch(AppCompatActivity activity, View transitionView, String imageUrl) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);

//        // 这里指定了共享的视图元素
//        ActivityOptionsCompat options = ActivityOptionsCompat
//                .makeSceneTransitionAnimation(activity, transitionView, SHARE_TRANSITION_NAME);
//
//        ActivityCompat.startActivity(activity, intent, options.toBundle());
        activity.startActivity(intent);
    }

    @Override
    protected void handleIntent(Intent intent) {
        mImageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(mImageUrl)){
            cvImage.setImageResource(R.drawable.img_default_face);
        }else{
            Glide.with(this).load(mImageUrl).placeholder(R.drawable.img_placeholder).fitCenter().into(cvImage);
        }
//        // 这里指定了被共享的视图元素
//        ViewCompat.setTransitionName(cvImage, SHARE_TRANSITION_NAME);
    }
}
