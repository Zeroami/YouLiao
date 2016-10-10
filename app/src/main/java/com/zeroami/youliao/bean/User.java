package com.zeroami.youliao.bean;

import com.avos.avoscloud.AVUser;
import com.zeroami.youliao.config.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：用户类</p>
 */
public class User implements Serializable,Cloneable{
    private String objectId;
    private String username;
    private String nickname;
    private String signature;
    private String avatar;

    public static User convertToUser(AVUser avUser){
        if(avUser == null) return null;
        User user = new User();
        user.objectId = avUser.getObjectId();
        user.username = avUser.getUsername();
        user.nickname = (String) avUser.get(Constant.User.NICKNAME);
        user.signature = (String) avUser.get(Constant.User.SIGNATURE);
        user.avatar = (String) avUser.get(Constant.User.AVATAR);
        return user;
    }

    public static AVUser convertToAVUser(User user){
        AVUser avUser = new AVUser();
        avUser.setObjectId(user.objectId);
        avUser.setUsername(user.username);
        avUser.put(Constant.User.NICKNAME,user.nickname);
        avUser.put(Constant.User.SIGNATURE,user.signature);
        avUser.put(Constant.User.AVATAR,user.avatar);
        return avUser;
    }

    public static List<User> convertToUserList(List<AVUser> avUserList){
        if(avUserList == null) return null;
        List<User> userList = new ArrayList<>();
        for(AVUser avUser : avUserList){
            userList.add(convertToUser(avUser));
        }
        return userList;
    }

    public static List<AVUser> convertToAVUserList(List<User> userList){
        List<AVUser> avUserList = new ArrayList<>();
        for(User user : userList){
            avUserList.add(convertToAVUser(user));
        }
        return avUserList;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "objectId='" + objectId + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
