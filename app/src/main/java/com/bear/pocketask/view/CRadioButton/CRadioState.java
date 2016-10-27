package com.bear.pocketask.view.CRadioButton;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CRadioState{
    CRadioButton crbtn;
    private int count=0;
    public CRadioState(CRadioButton crbtn){
        this.crbtn=crbtn;
    }

    public void changeCRState(){
        count=(count+1)%2;
        this.setCRState(count);
    }

    public void setCRState(int state){
        crbtn.setApperance(state);
        switch (state)
        {
            case 0:
                crbtn.listener.openState();
                break;
            case 1:
                crbtn.listener.closeState();
                break;
        }
    }

}
