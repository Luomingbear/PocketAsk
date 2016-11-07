package com.bear.pocketask.info;

/**
 * 评论的数据信息
 * Created by bear on 16/11/7.
 */

public class CommentInfo extends UserInfo {

    private String comment_text; //评论的内容

    public CommentInfo(int UId, String UHeadPortraitUrl, String UName, String comment_text) {
        super(UId, UHeadPortraitUrl, UName);
        this.comment_text = comment_text;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }
}
