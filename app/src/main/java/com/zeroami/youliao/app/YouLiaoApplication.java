package com.zeroami.youliao.app;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.zeroami.commonlib.common.LBaseApplication;
import com.zeroami.youliao.BuildConfig;
import com.zeroami.youliao.data.handler.ChatMessageHandler;
import com.zeroami.youliao.data.http.PushManager;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：YouLiaoApplication</p>
 */
public class YouLiaoApplication extends LBaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        //Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        AVOSCloud.initialize(this, "c1McCc3Q0HqGImWbP2wxcIgg-gzGzoHsz", "ESrhp4jt9ERi5qY8Fm9Fg25W");
        PushManager.getInstance().initialize(this);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,new ChatMessageHandler());
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
