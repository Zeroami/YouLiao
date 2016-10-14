package com.zeroami.youliao.config;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：常量类</p>
 */
public class Constant {

    public static final class User {
        public static final String USERNAME = "username";
        public static final String NICKNAME = "nickname";
        public static final String SIGNATURE = "signature";
        public static final String AVATAR = "avatar";
        public static final String INSTALLATION = "installation";
    }

    public static final class AddRequest {
        public static final String CLASS_NAME = "AddRequest";
        public static final String FROM_USER_ID = "fromUserId";
        public static final String TO_USER_ID = "toUserId";
        public static final String STATUS = "status";
        public static final String IS_READ = "isRead";
        public static final String EXTRA = "extra";
    }

    public static final class Followee{
        public static final String CLASS_NAME = "_Followee";
        public static final String FOLLOWEE = "followee";
        public static final String USER = "user";
    }

    public static final class Follower{
        public static final String CLASS_NAME = "_Follower";
        public static final String FOLLOWER = "follower";
        public static final String USER = "user";
    }

    public static final int CODE_USERNAME_IS_EXIST = 202;   // 用户名已存在

    public static final int SIGNATURE_MAX_WORD_NUM = 120;   // 个性签名的最大字数

    public static final int INIT_SIZE = 20;      // 初始大小
    public static final int PAGE_SIZE = 10;      // 每页大小

    public static final int EXPRESS_TOTAL_COUNT = 74;
    public static final int EXPRESS_COL_SIZE = 7;
    public static final int EXPRESS_ROW_SIZE = 3;
    public static final String EXPRESS_PREFIX = "emoji_";

    public static final class Action{
        public static final String ADD_FRIEND = "com.zeroami.youliao.action.ADD_FRIEND";
        public static final String NEW_FRIEND_ADDED = "com.zeroami.youliao.action.NEW_FRIEND_ADDED";
        public static final String RECEIVE_CHAT_MESSAGE = "com.zeroami.youliao.action.RECEIVE_CHAT_MESSAGE";
        public static final String DELETE_FRIEND = "com.zeroami.youliao.action.DELETE_FRIEND";
    }

}
