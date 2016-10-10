package com.zeroami.youliao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Adapter</p>
 */
public class ContactsAdapter extends BaseQuickAdapter<User> {
    private Context mContext;

    public ContactsAdapter(Context context,List<User> data) {
        super(R.layout.item_contacts, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        baseViewHolder.setText(R.id.tv_nickname, user.getNickname())
                .setText(R.id.tv_signature, TextUtils.isEmpty(user.getSignature()) ? LRUtils.getString(R.string.friend_without_signature) : user.getSignature());
        LCircleImageView cvCircleAvatar = baseViewHolder.getView(R.id.cv_circle_avatar);
        if (TextUtils.isEmpty(user.getAvatar())) {
             cvCircleAvatar.setImageResource(R.mipmap.img_default_face);
        } else {
            Glide.with(mContext).load(user.getAvatar()).centerCrop().into(cvCircleAvatar);
        }

    }
}
