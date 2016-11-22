package com.zeroami.youliao.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.PersonalInfoContract;
import com.zeroami.youliao.presenter.activity.PersonalInfoPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;
import me.iwf.photopicker.PhotoPicker;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：个人信息页</p>
 */
public class PersonalInfoActivity extends BaseMvpActivity<PersonalInfoContract.Presenter> implements PersonalInfoContract.View, View.OnClickListener {


    public static final String EXTRA_USER = "user";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.cv_circle_avatar)
    LCircleImageView cvCircleAvatar;
    @Bind(R.id.tv_change_avatar_tips)
    TextView tvChangeAvatarTips;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.et_signature)
    EditText etSignature;
    @Bind(R.id.tv_remain_word_tips)
    TextView tvRemainWordTips;
    @Bind(R.id.tv_save_change)
    TextView tvSaveChange;


    private User mUser;
    private TextWatcher mTextWatcher;

    private boolean mIsAvatarChanged;
    private String mAvatarLocalPath;
    private User mChangedUser;

    @Override
    protected PersonalInfoContract.Presenter createPresenter() {
        return new PersonalInfoPresenter(this);
    }

    @Override
    protected void handleExtras(Bundle extras) {
        mUser = (User) extras.getSerializable(EXTRA_USER);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }


    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initToolbar();
        initView();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(LRUtils.getString(R.string.persontal_info));
        setSupportActionBar(toolbar);
    }

    private void initView() {
        if (TextUtils.isEmpty(mUser.getAvatar())) {
            cvCircleAvatar.setImageResource(R.drawable.img_default_face);
        } else {
            Glide.with(this).load(mUser.getAvatar()).centerCrop().into(cvCircleAvatar);
        }
        tvUsername.setText(mUser.getUsername());
        tvNickname.setText(mUser.getNickname());
        etNickname.setText(mUser.getNickname());
        if (TextUtils.isEmpty(mUser.getSignature())) {
            tvSignature.setText(LRUtils.getString(R.string.without_signature));
            etSignature.setText("");
        } else {
            tvSignature.setText(mUser.getSignature());
            etSignature.setText(mUser.getSignature());
        }
        tvRemainWordTips.setText(String.format(LRUtils.getString(R.string.format_remain_word), Constant.SIGNATURE_MAX_WORD_NUM - etSignature.getText().toString().length()));

        if (mTextWatcher == null) {
            mTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String signature = etSignature.getText().toString();
                    if (signature.length() > Constant.SIGNATURE_MAX_WORD_NUM) {
                        signature = signature.substring(0, Constant.SIGNATURE_MAX_WORD_NUM);
                        etSignature.setText(signature);
                        etSignature.setSelection(signature.length());
                    }
                    tvRemainWordTips.setText(String.format(LRUtils.getString(R.string.format_remain_word), Constant.SIGNATURE_MAX_WORD_NUM - signature.length()));
                }
            };
            etSignature.addTextChangedListener(mTextWatcher);
        }
        cvCircleAvatar.setOnClickListener(this);
        tvSaveChange.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                getMvpPresenter().doEdit();
                break;

        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_circle_avatar:
                getMvpPresenter().doAvatarClick();
                break;
            case R.id.tv_save_change:
                updateChangedUser();
                getMvpPresenter().doSaveChange(mChangedUser);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos != null && photos.size() > 0){
                    mIsAvatarChanged = true;
                    mAvatarLocalPath = photos.get(0);
                    Glide.with(this).load(mAvatarLocalPath).centerCrop().into(cvCircleAvatar);
                }
            }
        }
    }

    /**
     * 选择图片
     */
    private void pickPhoto() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    /**
     * 更新要保存的用户对象
     */
    private void updateChangedUser() {
        mChangedUser = mUser.clone();
        mChangedUser.setNickname(etNickname.getText().toString());
        mChangedUser.setSignature(etSignature.getText().toString());
        mChangedUser.setAvatar(mAvatarLocalPath == null ? mUser.getAvatar() : mAvatarLocalPath);
    }

    @Override
    public void hideTextView() {
        tvNickname.setVisibility(View.GONE);
        tvSignature.setVisibility(View.GONE);
    }

    @Override
    public void showEditView() {
        tvChangeAvatarTips.setVisibility(View.VISIBLE);
        etNickname.setVisibility(View.VISIBLE);
        etSignature.setVisibility(View.VISIBLE);
        tvRemainWordTips.setVisibility(View.VISIBLE);
        tvSaveChange.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTextView() {
        tvNickname.setVisibility(View.VISIBLE);
        tvSignature.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEditView() {
        tvChangeAvatarTips.setVisibility(View.GONE);
        etNickname.setVisibility(View.GONE);
        etSignature.setVisibility(View.GONE);
        tvRemainWordTips.setVisibility(View.GONE);
        tvSaveChange.setVisibility(View.GONE);
    }

    @Override
    public boolean isContentChanged() {
        return mIsAvatarChanged || !mUser.getNickname().equals(etNickname.getText().toString())
                || !mUser.getSignature().equals(etSignature.getText().toString());
    }

    @Override
    public void showAbandonChangeTips() {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle(LRUtils.getString(R.string.dialog_tips))
                .setMessage(LRUtils.getString(R.string.abandon_persontal_info_change))
                .setPositiveButton(R.string.confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        getMvpPresenter().doAbandonChange();
                    }
                })
                .setNegativeButton(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void restoreView() {
        mIsAvatarChanged = false;
        mAvatarLocalPath = null;
        initView();
    }

    @Override
    public void updateView() {
        mUser = mChangedUser;
        initView();
    }

    @Override
    public void changeAvatar() {
        pickPhoto();
    }

    @Override
    public void gotoImage() {
        ImageActivity.launch(this,cvCircleAvatar,mUser.getAvatar());
    }
}
