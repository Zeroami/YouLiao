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
import com.zeroami.commonlib.utils.LT;
import com.zeroami.youliao.R;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.PushManager;
import com.zeroami.youliao.utils.RandomUtils;

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
        if (action.equals(Constant.Action.ADD_FRIEND)){
            receiveAddRequest(context,intent);
        }
    }

    /**
     * 接收到添加请求
     * @param context
     * @param intent
     */
    private void receiveAddRequest(Context context,Intent intent){
        LRxBus.getDefault().postTag(Constant.Action.ADD_FRIEND);    // 发送一个好友请求事件
        String avosData = intent.getStringExtra(AVOS_DATA);
        if (!TextUtils.isEmpty(avosData)) {
            try {
                JSONObject json = new JSONObject(avosData);
                LL.d(json);
                if (null != json) {
                    String alertStr = json.getString(PushManager.PUSH_DATA_ALERT);
                    Intent notificationIntent = new Intent(context, NotificationDispatcherBroadcastReceiver.class);
                    PendingIntent pi = PendingIntent.getBroadcast(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationIntent.putExtra(NotificationDispatcherBroadcastReceiver.EXTRA_ACTION, intent.getAction());
                    // 发送通知
                    Notification notification = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_logo)
                            .setTicker(LRUtils.getString(R.string.notification_receive_add_request))
                            .setWhen(System.currentTimeMillis())
                            .setContentTitle(LRUtils.getString(R.string.app_name))
                            .setContentText(alertStr)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .build();

                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(new Random().nextInt(),notification);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}