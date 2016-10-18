package com.zeroami.youliao.data.local;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.bean.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：SharedPreferences管理者</p>
 */
public class SPManager {

    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_IS_EXIT_APP = "is_exit_app";
    private static final String KEY_CURRENT_USER = "current_user";
    private static final String KEY_CONVERSATION_IDS = "conversation_ids";

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

    /**
     * 保存会话
     * @param conversationId
     */
    public void saveConversationId(String conversationId){
        List<String> conversationIds = getConversationIds();
        conversationIds.add(conversationId);
        saveConversationIds(conversationIds);
    }

    /**
     * 删除会话
     * @param conversationId
     */
    public void removeConversationId(String conversationId){
        List<String> conversationIds = getConversationIds();
        conversationIds.remove(conversationId);
        saveConversationIds(conversationIds);
    }

    /**
     * 保存会话列表
     * @param conversationIds
     */
    public void saveConversationIds(List<String> conversationIds){
        String str = TextUtils.join(",", conversationIds);
        LSPUtils.put(KEY_CONVERSATION_IDS, str);
    }

    /**
     * 获取会话列表
     * @return
     */
    public List<String> getConversationIds(){
        String str = LSPUtils.get(KEY_CONVERSATION_IDS, "");
        if (TextUtils.isEmpty(str)){
            return new ArrayList<>();
        }else{
            return Arrays.asList(str.split(","));
        }
    }

    /**
     * 通过成员id保存会话
     * @param memberId
     * @param conversationId
     */
    public void saveConversationIdByMemberId(String memberId,String conversationId){
        LSPUtils.put(memberId,conversationId);
    }

    /**
     * 通过成员id获取会话
     * @param memberId
     * @return
     */
    public String getConversationIdByMemberId(String memberId){
        return LSPUtils.get(memberId,"");
    }

    /**
     * 获取会话未读数量
     * @param conversationId
     * @return
     */
    public int getConversationUnreadCount(String conversationId){
        return LSPUtils.get(conversationId,0);
    }

    /**
     * 会话未读数量加一
     * @param conversationId
     */
    public void increamentConversationUnreadCount(String conversationId){
        int count = LSPUtils.get(conversationId,0);
        LSPUtils.put(conversationId,++count);
    }

    /**
     * 标记会话为已读
     * @param conversationId
     */
    public void markConversationRead(String conversationId){
        LSPUtils.remove(conversationId);
    }

}
