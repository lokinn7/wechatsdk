package com.riversoft.weixin.app.user;

/**
 * @author liuyijie
 */
public class Watermark {
    private long timestamp;
    private String appid;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
