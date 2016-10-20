package com.zeroami.youliao.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：自定义推送接受者</p>
 */
public class PushBroadcaseReceiver extends BroadcastReceiver {

    public final static String AVOS_DATA = "com.avoscloud.Data";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LL.d(action);
        if (action.equals(Constant.Action.DELETE_FRIEND)) {
            receivePushWithoutNotification(intent);
        } else {
            receivePushWithNotification(context, intent);
        }
    }


    /**
     * 接收到带通知的推送信息
     *
     * @param context
     * @param intent
     */
    private void receivePushWithNotification(Context context, Intent intent) {
        String avosData = intent.getStringExtra(AVOS_DATA);
        if (!TextUtils.isEmpty(avosData)) {
            try {
                JSONObject json = new JSONObject(avosData);
                if (json != null) {
                    String alertStr = json.getString(PushManager.PUSH_DATA_ALERT);
                    Intent notificationIntent = new Intent(context, NotificationDispatcherBroadcastReceiver.class);
                    notificationIntent.putExtra(NotificationDispatcherBroadcastReceiver.EXTRA_ACTION, intent.getAction());
                    PendingIntent pi = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // 发送通知
                    Notification notification = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setTicker(alertStr)
                            .setWhen(System.currentTimeMillis())
                            .setContentTitle(LRUtils.getString(R.string.app_name))
                            .setContentText(alertStr)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();

                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(new Random().nextInt(), notification);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LRxBus.getDefault().postTag(intent.getAction());    // 发送事件
        }
    }


    /**
     * 接收到不带通知的推送信息
     */
    private void receivePushWithoutNotification(Intent intent) {
        String avosData = intent.getStringExtra(AVOS_DATA);
        String friendId = "";
        if (!TextUtils.isEmpty(avosData)) {
            try {
                JSONObject json = new JSONObject(avosData);
                if (json != null) {
                    friendId = json.getString(PushManager.PUSH_DATA_ALERT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LRxBus.getDefault().post(friendId, intent.getAction());    // 发送事件
        }
    }
}