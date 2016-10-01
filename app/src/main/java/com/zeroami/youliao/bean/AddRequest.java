package com.zeroami.youliao.bean;

import com.avos.avoscloud.AVObject;
import com.zeroami.youliao.config.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：好友添加请求</p>
 */
public class AddRequest{
    public static final int STATUS_WAIT = 0;    // 等待同意
    public static final int STATUS_DONE = 1;    // 同意

    private String objectId;
    private String fromUserId;
    private String toUserId;
    private int status;
    private boolean isRead;
    private String extra;

    // 辅助字段
    private User fromUser;
    private User toUser;

    public static AddRequest convertToAddRequest(AVObject avObject){
        AddRequest addRequest = new AddRequest();
        addRequest.objectId = avObject.getObjectId();
        addRequest.fromUserId = avObject.getString(Constant.AddRequest.FROM_USER_ID);
        addRequest.toUserId = avObject.getString(Constant.AddRequest.TO_USER_ID);
        addRequest.status = avObject.getInt(Constant.AddRequest.STATUS);
        addRequest.isRead = avObject.getBoolean(Constant.AddRequest.IS_READ);
        addRequest.extra = avObject.getString(Constant.AddRequest.EXTRA);
        return addRequest;
    }

    public static AVObject convertToAVObject(AddRequest addRequest){
        AVObject avObject = new AVObject(Constant.AddRequest.CLASS_NAME);
        avObject.setObjectId(addRequest.objectId);
        avObject.put(Constant.AddRequest.FROM_USER_ID, addRequest.fromUserId);
        avObject.put(Constant.AddRequest.TO_USER_ID,addRequest.toUserId);
        avObject.put(Constant.AddRequest.STATUS,addRequest.status);
        avObject.put(Constant.AddRequest.IS_READ,addRequest.isRead);
        avObject.put(Constant.AddRequest.EXTRA,addRequest.extra);
        return avObject;
    }

    public static List<AddRequest> convertToAddRequestList(List<AVObject> avObjectList){
        List<AddRequest> addRequestList = new ArrayList<>();
        for (AVObject avObject : avObjectList){
            addRequestList.add(convertToAddRequest(avObject));
        }
        return addRequestList;
    }

    public static List<AVObject> convertToAVObjecyList(List<AddRequest> addRequestList){
        List<AVObject> avObjectList = new ArrayList<>();
        for (AddRequest addRequest : addRequestList){
            avObjectList.add(convertToAVObject(addRequest));
        }
        return avObjectList;
    }

    @Override
    public String toString() {
        return "AddRequest{" +
                "objectId='" + objectId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", status=" + status +
                ", isRead=" + isRead +
                ", extra='" + extra + '\'' +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                '}';
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }


    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

}
