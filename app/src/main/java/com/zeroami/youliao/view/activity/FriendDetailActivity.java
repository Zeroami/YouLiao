package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.activity.FriendDetailContract;
import com.zeroami.youliao.presenter.activity.FriendDetailPresenter;

import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：朋友详情页</p>
 */
public class FriendDetailActivity extends BaseMvpActivity<FriendDetailContract.Presenter> implements FriendDetailContract.View, View.OnClickListener {


    public static final String EXTRA_USER = "user";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.cv_circle_avatar)
    LCircleImageView cvCircleAvatar;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.tv_add_friend)
    TextView tvAddFriend;
    @Bind(R.id.tv_send_message)
    TextView tvSendMessage;
    @Bind(R.id.tv_already_send_request_tips)
    TextView tvAlreadySendRequestTips;
    @Bind(R.id.tv_delete_friend)
    TextView tvDeleteFriend;

    private User mUser;
    private String mExtra = "";


    @Override
    protected FriendDetailContract.Presenter createPresenter() {
        return new FriendDetailPresenter(this);
    }

    @Override
    protected void handleExtras(Bundle extras) {
        mUser = (User) extras.getSerializable(EXTRA_USER);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_detail;
    }


    @Override
    protected void initialize(Bundle savedInstanceState) {
        setSwipeBackEnable(true);
        initToolbar();
        initView();
        initListener();
    }

    @Override
    protected void onInitialized() {
        getMvpPresenter().doViewInitialized();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(LRUtils.getString(R.string.friend_detail));
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
        if (TextUtils.isEmpty(mUser.getSignature())) {
            tvSignature.setText(LRUtils.getString(R.string.friend_without_signature));
        } else {
            tvSignature.setText(mUser.getSignature());
        }
    }

    private void initListener(){
        cvCircleAvatar.setOnClickListener(this);
        tvAddFriend.setOnClickListener(this);
        tvSendMessage.setOnClickListener(this);
        tvDeleteFriend.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_circle_avatar:
                getMvpPresenter().doAvatarClick();
                break;
            case R.id.tv_add_friend:
                getMvpPresenter().doEditExtra();
                break;
            case R.id.tv_send_message:
                getMvpPresenter().doSendMessage();
                break;
            case R.id.tv_delete_friend:
                getMvpPresenter().doDeleteFriend();
                break;
        }
    }

    @Override
    public User getUser() {
        return mUser;
    }

    @Override
    public void editExtra() {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_edit_extra, null);
        final EditText etExtra = (EditText) view.findViewById(R.id.et_extra);
        etExtra.setText(mExtra);
        etExtra.setSelection(2, mExtra.length());
        etExtra.requestFocus();
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setTitle(LRUtils.getString(R.string.add_extra))
                .setContentView(view)
                .setPositiveButton(R.string.confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        mExtra = etExtra.getText().toString();
                        getMvpPresenter().doAddFriend();
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
    public void setDefaultExtra(String extra) {
        mExtra = extra;
    }

    @Override
    public String getExtra() {
        return mExtra;
    }

    @Override
    public void showAddFriend() {
        tvAddFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddFriend() {
        tvAddFriend.setVisibility(View.GONE);
    }

    @Override
    public void showSendMessage() {
        tvSendMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSendMessage() {
        tvSendMessage.setVisibility(View.GONE);
    }

    @Override
    public void showDeleteFriend() {
        tvDeleteFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDeleteFriend() {
        tvDeleteFriend.setVisibility(View.GONE);
    }

    @Override
    public void showAlreadSendRequestTips() {
        tvAlreadySendRequestTips.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAlreadSendRequestTips() {
        tvAlreadySendRequestTips.setVisibility(View.GONE);
    }

    @Override
    public void gotoImage() {
        ImageActivity.launch(this,cvCircleAvatar,mUser.getAvatar());
    }

    @Override
    public void gotoChat() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatActivity.EXTRA_USER,mUser);
        LPageUtils.startActivity(this, ChatActivity.class, bundle, false);
    }
}
