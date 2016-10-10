package com.zeroami.youliao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeroami.commonlib.utils.LAppUtils;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.view.activity.MainActivity;
import com.zeroami.youliao.view.activity.NewFriendActivity;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：通知分发器，对Notification的点击统一处理，根据不同的action标识分发跳转到相应的界面</p>
 */
public class NotificationDispatcherBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_ACTION = "action";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
//        if (LAppUtils.isBackground()){
//            gotoMain(context,action);
//        }
        if (action.equals(Constant.Action.ADD_FRIEND)){
            gotoNewFriend(context);
        } else if(action.equals(Constant.Action.NEW_FRIEND_ADDED)){
            gotoMain(context);
        }
    }

    private void gotoMain(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void gotoNewFriend(Context context){
        Intent intent = new Intent(context,NewFriendActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
