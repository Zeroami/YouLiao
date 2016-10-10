package com.zeroami.youliao.data.http;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.view.activity.SplashActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：推送管理者</p>
 */
public class PushManager {

    public static final String PUSH_CHANNELS = "channels";
    public static final String PUSH_DATA_ALERT = "alert";
    public static final String PUSH_DATA_ACTION = "action";

    private static volatile PushManager sInstace;
    private Context mContext;

    private PushManager() {
    }

    public static PushManager getInstance() {
        if (sInstace == null) {
            synchronized (PushManager.class) {
                if (sInstace == null) {
                    sInstace = new PushManager();
                }
            }
        }
        return sInstace;
    }

    /**
     * 启动推送服务
     *
     * @param context
     */
    public void initialize(Context context) {
        mContext = context;
        PushService.setDefaultPushCallback(context, SplashActivity.class);
    }

    /**
     * 订阅推送频道（应在登陆成功之后）
     *
     * @param avUser
     */
    public void subscribePushChannel(final AVUser avUser) {
        PushService.subscribe(mContext, avUser.getObjectId(), SplashActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 关联installation到用户表等操作……
                    UserManager.getInstance().attachUserToPushChannel(avUser);
                }
            }
        });
    }

    /**
     * 解除订阅推送频道（应在退出登陆之前）
     *
     * @param avUser
     */
    public void unsubscribePushChannel(final AVUser avUser) {
        PushService.unsubscribe(mContext, avUser.getObjectId());
        AVInstallation.getCurrentInstallation().saveInBackground();
    }

    /**
     * 推送消息
     *
     * @param toUserId
     * @param alert
     * @param action
     */
    public void pushMessage(String toUserId, String alert, String action) {
        AVQuery query = AVInstallation.getQuery();
        query.whereContains(PUSH_CHANNELS, toUserId);
        AVPush push = new AVPush();
        push.setQuery(query);
        push.setPushToAndroid(true);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(PUSH_DATA_ALERT, alert);
        dataMap.put(PUSH_DATA_ACTION, action);
        push.setData(dataMap);
        push.sendInBackground();
    }
}