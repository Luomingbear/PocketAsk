package com.bear.pocketask.NetProtocol;

/**
 * Created by Administrator on 2016/11/7.
 */

public class CancelProtocol extends TRprotocol {
    public CancelProtocol() {
        super();
    }

    public CancelProtocol(String content){
        user=content;
    }
    public void setIntent(){
        intent+=user;
    }
}
