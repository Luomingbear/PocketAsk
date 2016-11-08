package com.bear.pocketask.info;

import java.util.Date;

/**
 * 我的提问的单个选项的数据类型
 * Created by bear on 16/11/5.
 */

public class MyQAInfo {

    private String question; //回答
    private int remindCount; //回答数
    private int isPrivate; //是否私密
    private Date date; //创建日期

    private QAType qaType = QAType.QUESTION;

    public MyQAInfo(String question, int remindCount, int isPrivate, Date date, QAType qaType) {
        this.question = question;
        this.remindCount = remindCount;
        this.isPrivate = isPrivate;
        this.date = date;
        this.qaType = qaType;
    }

    public MyQAInfo(String question, int remindCount, int isPrivate, Date date) {
        this.question = question;
        this.remindCount = remindCount;
        this.isPrivate = isPrivate;
        this.date = date;
    }

    public QAType getQaType() {
        return qaType;
    }

    public void setQaType(QAType qaType) {
        this.qaType = qaType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(int remindCount) {
        this.remindCount = remindCount;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public enum QAType {
        QUESTION,

        RESPOND
    }
}
