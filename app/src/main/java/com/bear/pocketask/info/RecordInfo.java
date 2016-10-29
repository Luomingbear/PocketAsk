package com.bear.pocketask.info;

import com.bear.pocketask.widget.record.RecordView;

/**
 * 语音控件的信息
 * Created by bear on 16/10/29.
 */

public class RecordInfo {
    private int recordId; //id
    private String recrodUrl; //音频文件的网络地址
    private boolean isPlay; //是否处于播放
    private RecordView.RecordMode recordMode = RecordView.RecordMode.BROADCAST; //默认是播放模式

    public RecordInfo(int recordId, String recrodUrl, boolean isPlay, RecordView.RecordMode recordMode) {
        this.recordId = recordId;
        this.recrodUrl = recrodUrl;
        this.isPlay = isPlay;
        this.recordMode = recordMode;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecrodUrl() {
        return recrodUrl;
    }

    public void setRecrodUrl(String recrodUrl) {
        this.recrodUrl = recrodUrl;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public RecordView.RecordMode getRecordMode() {
        return recordMode;
    }

    public void setRecordMode(RecordView.RecordMode recordMode) {
        this.recordMode = recordMode;
    }
}
