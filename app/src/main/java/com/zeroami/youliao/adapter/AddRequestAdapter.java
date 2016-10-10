package com.zeroami.youliao.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：朋友添加请求列表Adapter</p>
 */
public class AddRequestAdapter extends BaseQuickAdapter<AddRequest> {
    private Context mContext;

    public AddRequestAdapter(Context context, List<AddRequest> data) {
        super(R.layout.item_new_friend, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AddRequest addRequest) {
        baseViewHolder.setText(R.id.tv_nickname, addRequest.getFromUser().getNickname())
                .setText(R.id.tv_extra, addRequest.getExtra())
                .setVisible(R.id.tv_agree, addRequest.getStatus() == AddRequest.STATUS_WAIT)
                .setVisible(R.id.tv_already_agree_tips, addRequest.getStatus() == AddRequest.STATUS_DONE)
                .addOnClickListener(R.id.tv_agree);
        LCircleImageView cvCircleAvatar = baseViewHolder.getView(R.id.cv_circle_avatar);
        if (TextUtils.isEmpty(addRequest.getFromUser().getAvatar())) {
            cvCircleAvatar.setImageResource(R.mipmap.img_default_face);
        } else {
            Glide.with(mContext).load(addRequest.getFromUser().getAvatar()).centerCrop().into(cvCircleAvatar);
        }
    }
}
