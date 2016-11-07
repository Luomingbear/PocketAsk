package com.bear.pocketask.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡片信息类
 * Created by bear on 16/10/1.
 */

public class CardItemInfo implements Parcelable {
    private String headPic; //头像
    private String userName; //用户名
    private String detailPic; //卡片详情图片
    private String detailUrl; //卡片详细的链接
    private String questions; //问题描述
    private int questionId; //问题id+
    private String inputText; //输入的文本
    private List<SelectorInfo> selectorList; //选项的数据源
    private CardMode cardMode = CardMode.TopTextBottomImage; //卡片的类型


    /**
     * 卡片的类型
     */
    public enum CardMode {
        //上面是语音 下面图片
        TopAudioBottomImage,

        //上面 文字 ,下面 选项
        TopTextBottomImage,

        //上面语音 ,下面选项
        TopAudioBottomSelector,

        //上面文字 ,下面选项
        TopTextBottomSelector,

    }

    public CardItemInfo() {
    }

    public CardItemInfo(String headPic, String userName, String detailPic, String detailUrl, String questions, int questionId, String inputText, List<SelectorInfo> selectorList, CardMode cardMode) {
        this.headPic = headPic;
        this.userName = userName;
        this.detailPic = detailPic;
        this.detailUrl = detailUrl;
        this.questions = questions;
        this.questionId = questionId;
        this.inputText = inputText;
        this.selectorList = selectorList;
        this.cardMode = cardMode;
    }

    public List<SelectorInfo> getSelectorList() {
        return selectorList;
    }

    public void setSelectorList(List<SelectorInfo> selectorList) {
        this.selectorList = selectorList;
    }

    public CardMode getCardMode() {
        return cardMode;
    }

    public void setCardMode(CardMode cardMode) {
        this.cardMode = cardMode;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headPic);
        dest.writeString(this.userName);
        dest.writeString(this.detailPic);
        dest.writeString(this.detailUrl);
        dest.writeString(this.questions);
        dest.writeInt(this.questionId);
        dest.writeString(this.inputText);
        dest.writeList(this.selectorList);
        dest.writeInt(this.cardMode == null ? -1 : this.cardMode.ordinal());
    }

    protected CardItemInfo(Parcel in) {
        this.headPic = in.readString();
        this.userName = in.readString();
        this.detailPic = in.readString();
        this.detailUrl = in.readString();
        this.questions = in.readString();
        this.questionId = in.readInt();
        this.inputText = in.readString();
        this.selectorList = new ArrayList<SelectorInfo>();
        in.readList(this.selectorList, SelectorInfo.class.getClassLoader());
        int tmpCardMode = in.readInt();
        this.cardMode = tmpCardMode == -1 ? null : CardMode.values()[tmpCardMode];
    }

    public static final Parcelable.Creator<CardItemInfo> CREATOR = new Parcelable.Creator<CardItemInfo>() {
        @Override
        public CardItemInfo createFromParcel(Parcel source) {
            return new CardItemInfo(source);
        }

        @Override
        public CardItemInfo[] newArray(int size) {
            return new CardItemInfo[size];
        }
    };
}
