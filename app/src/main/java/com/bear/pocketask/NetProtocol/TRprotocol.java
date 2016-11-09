package com.bear.pocketask.NetProtocol;

/**
 * Created by Administrator on 2016/11/7.
 */

public abstract class TRprotocol {
    int function; //0register 1login 2cancel  3send 4response
    final static String DIVIDE_EXTERNAL="€";
    final static String DIVIDE_INTERNAL="£";

    String user;
    String intent;

    public TRprotocol(){
        intent=function+DIVIDE_EXTERNAL;
    }

    public String getIntent() {
        return intent;
    }

    public abstract void setIntent();

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
