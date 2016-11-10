package com.bear.pocketask.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个选项的信息
 * Created by bear on 16/10/20.
 */

public class SelectorInfo implements Parcelable {
    private String content ; //内容

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    protected SelectorInfo(Parcel in) {
        this.content = in.readString();
    }

    public static final Parcelable.Creator<SelectorInfo> CREATOR = new Parcelable.Creator<SelectorInfo>() {
        @Override
        public SelectorInfo createFromParcel(Parcel source) {
            return new SelectorInfo(source);
        }

        @Override
        public SelectorInfo[] newArray(int size) {
            return new SelectorInfo[size];
        }
    };
}
