package com.zeroami.commonlib.rx.rxbus;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：RxBus事件的包装</p>
 */
public class LRxBusEvent {
    private String tag;
    private Object data;

    public LRxBusEvent(String tag, Object data) {
        this.tag = tag;
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
