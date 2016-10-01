package com.zeroami.youliao.utils;

import com.zeroami.commonlib.utils.LCipherUtils;
import com.zeroami.commonlib.utils.LL;

import java.util.Random;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：随机数工具类</p>
 */
public class RandomUtils {

    public static Random sRandom = new Random();

    /**
     * 生成随机数字串
     * @param length
     * @return
     */
    public static String createRandomNumString(int length){
        long num = Math.abs(sRandom.nextLong());
        String string = String.format("%0"+length+"d", num);
        return string.substring(0,length);
    }

    /**
     * 生成随机MD5CHAUNG
     * @return
     */
    public static String createRandomMD5String(){
        return createRandomMD5String(32);
    }

    /**
     * 生成随机MD5CHAUNG
     * @return
     */
    public static String createRandomMD5String(int length){
        return LCipherUtils.md5(System.currentTimeMillis()+"").substring(0,length);
    }
}
