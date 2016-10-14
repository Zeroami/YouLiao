package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.fragment.MeContract;
import com.zeroami.youliao.presenter.fragment.MePresenter;
import com.zeroami.youliao.view.activity.LoginActivity;
import com.zeroami.youliao.view.activity.PersonalInfoActivity;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：我的</p>
 */
public class MeFragment extends BaseMvpFragment<MeContract.Presenter> implements MeContract.View, View.OnClickListener {

    @Bind(R.id.cv_circle_avatar)
    LCircleImageView cvCircleAvatar;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.rl_personal_info_area)
    RelativeLayout rlPersonalInfoArea;
    @Bind(R.id.ll_my_qrcode)
    LinearLayout llMyQrcode;
    @Bind(R.id.ll_my_photos)
    LinearLayout llMyPhotos;
    @Bind(R.id.ll_my_collection)
    LinearLayout llMyCollection;
    @Bind(R.id.ll_settings)
    LinearLayout llSettings;
    @Bind(R.id.ll_logout)
    LinearLayout llLogout;

    private User mCurrentUser;
    private boolean mIsViewCreated;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    protected MeContract.Presenter createPresenter() {
        return new MePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {
        mIsViewCreated = true;
        initView();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
    }

    private void initView() {
        if (mCurrentUser != null){
            if (TextUtils.isEmpty(mCurrentUser.getAvatar())) {
                cvCircleAvatar.setImageResource(R.drawable.img_default_face);
            } else {
                Glide.with(this).load(mCurrentUser.getAvatar()).centerCrop().into(cvCircleAvatar);
            }
            tvNickname.setText(mCurrentUser.getNickname());
            if (TextUtils.isEmpty(mCurrentUser.getSignature())) {
                tvSignature.setText(LRUtils.getString(R.string.without_signature));
            } else {
                tvSignature.setText(mCurrentUser.getSignature());
            }
        }
    }

    private void initListener() {
        rlPersonalInfoArea.setOnClickListener(this);
        llMyQrcode.setOnClickListener(this);
        llMyPhotos.setOnClickListener(this);
        llMyCollection.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        llLogout.setOnClickListener(this);
    }

    /**
     * 更新当前用户信息
     * @param currentUser
     */
    public void updateCurrentUserInfo(User currentUser) {
        mCurrentUser = currentUser;
        if (mIsViewCreated){
            initView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_personal_info_area:
                getMvpPresenter().doPersonalInfoAreaClick();
                break;
            case R.id.ll_my_qrcode:
                LT.show("my qrcode");
                break;
            case R.id.ll_my_photos:
                LT.show("my photos");
                break;
            case R.id.ll_my_collection:
                LT.show("my collection");
                break;
            case R.id.ll_settings:
                LT.show("settings");
                break;
            case R.id.ll_logout:
                getMvpPresenter().doLogout();
                break;
        }
    }

    @Override
    public void gotoPersonalInfo() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PersonalInfoActivity.EXTRA_USER,mCurrentUser);
        LPageUtils.startActivity(getAttachActivity(), PersonalInfoActivity.class, bundle, false);
    }

    @Override
    public void gotoLogin() {
        LPageUtils.startActivity(getAttachActivity(), LoginActivity.class, true);
    }
}
