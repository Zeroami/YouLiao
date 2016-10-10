package com.zeroami.youliao.data.local;

import com.google.gson.Gson;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.bean.User;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：SharedPreferences管理者</p>
 */
public class SPManager {

    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_IS_EXIT_APP = "is_exit_app";
    private static final String KEY_CURRENT_USER = "current_user";

    private static volatile SPManager sInstace;

    private SPManager() {
    }

    public static SPManager getInstance() {
        if (sInstace == null) {
            synchronized (SPManager.class) {
                if (sInstace == null) {
                    sInstace = new SPManager();
                }
            }
        }
        return sInstace;
    }

    /**
     * 登陆
     */
    public void login(){
        LSPUtils.put(KEY_IS_LOGIN, true);
    }

    /**
     * 保存当前用户
     * @param user
     */
    public void saveCurrentUser(User user){
        LSPUtils.put(KEY_CURRENT_USER, new Gson().toJson(user));
    }

    /**
     * 获取当前用户
     * @return
     */
    public User getCurrentUser(){
        String userJson = LSPUtils.get(KEY_CURRENT_USER, "");
        return new Gson().fromJson(userJson, User.class);
    }

    /**
     * 获取登陆状态
     * @return
     */
    public boolean getLoginStatus() {
        return LSPUtils.get(KEY_IS_LOGIN, false);
    }

    /**
     * 退出登陆
     */
    public void logout() {
        LSPUtils.remove(KEY_IS_LOGIN);
        LSPUtils.remove(KEY_IS_EXIT_APP);
    }

    /**
     * 获取退出状态
     * @return
     */
    public boolean getExitAppStatus() {
        return LSPUtils.get(KEY_IS_EXIT_APP, true);
    }

    /**
     * 进入App
     */
    public void enterApp() {
        LSPUtils.put(KEY_IS_EXIT_APP, false);
    }

    /**
     * 退出App
     */
    public void exitApp() {
        LSPUtils.put(KEY_IS_EXIT_APP, true);
    }

}
