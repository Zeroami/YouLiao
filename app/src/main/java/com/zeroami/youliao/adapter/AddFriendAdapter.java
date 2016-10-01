package com.zeroami.youliao.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：添加朋友搜索列表Adapter</p>
 */
public class AddFriendAdapter extends BaseQuickAdapter<User> {
    private Context mContext;
    public AddFriendAdapter(Context context,List<User> data) {
        super(R.layout.item_add_friend, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        baseViewHolder.setText(R.id.tv_username,String.format(LRUtils.getString(R.string.format_username),user.getUsername()))
                .setText(R.id.tv_nickname, String.format(LRUtils.getString(R.string.format_nickname), user.getNickname()));
        LCircleImageView cvCircleAvatar = baseViewHolder.getView(R.id.cv_circle_avatar);
        if (TextUtils.isEmpty(user.getAvatar())) {
            cvCircleAvatar.setImageResource(R.mipmap.img_default_face);
        } else {
            Glide.with(mContext).load(user.getAvatar()).centerCrop().into(cvCircleAvatar);
        }
    }
}
