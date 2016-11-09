package com.bear.pocketask.NetProtocol;

/**
 * Created by Administrator on 2016/11/7.
 */

public class ResponseProtocol extends TRprotocol {
    String userR;
    String text;

    public ResponseProtocol() {
        super();
    }

    public ResponseProtocol(String content){

    }
    public void setIntent(){
        intent+=user+DIVIDE_INTERNAL+userR+DIVIDE_INTERNAL+text;
    }

    public String getUserR() {
        return userR;
    }

    public void setUserR(String userR) {
        this.userR = userR;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
