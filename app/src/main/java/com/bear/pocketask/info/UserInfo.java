package com.bear.pocketask.info;

/**
 * 用户数据信息
 * Created by bear on 16/11/7.
 */

public class UserInfo {
    private int UId;
    private String UHeadPortraitUrl;
    private String UName;

    public UserInfo(int UId, String UHeadPortraitUrl, String UName) {
        this.UId = UId;
        this.UHeadPortraitUrl = UHeadPortraitUrl;
        this.UName = UName;
    }

    public int getUId() {
        return UId;
    }

    public void setUId(int UId) {
        this.UId = UId;
    }

    public String getUHeadPortraitUrl() {
        return UHeadPortraitUrl;
    }

    public void setUHeadPortraitUrl(String UHeadPortraitUrl) {
        this.UHeadPortraitUrl = UHeadPortraitUrl;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }
}
