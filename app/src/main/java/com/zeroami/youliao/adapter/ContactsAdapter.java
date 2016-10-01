package com.zeroami.youliao.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Adapter</p>
 */
public class ContactsAdapter extends BaseQuickAdapter<User> {
    public ContactsAdapter(List<User> data) {
        super(R.layout.item_contacts, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {

    }
}
