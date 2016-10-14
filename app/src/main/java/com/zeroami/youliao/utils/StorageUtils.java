package com.zeroami.youliao.utils;

import com.avos.avoscloud.im.v2.AVIMClient;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：变量存取工具类</p>
 */
public class StorageUtils {

    private static Map<Class,Object> mMap = new HashMap<>();

    public static void setAVIMClient(AVIMClient avimClient){
        mMap.put(AVIMClient.class,avimClient);
    }

    public static AVIMClient getAVIMClient(){
        return (AVIMClient) mMap.get(AVIMClient.class);
    }
}
