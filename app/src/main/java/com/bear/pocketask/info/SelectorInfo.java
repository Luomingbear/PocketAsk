package com.bear.pocketask.info;

/**
 * 单个选项的信息
 * Created by bear on 16/10/20.
 */

public class SelectorInfo {
    private String content; //内容

    public SelectorInfo() {
    }

    public SelectorInfo(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
