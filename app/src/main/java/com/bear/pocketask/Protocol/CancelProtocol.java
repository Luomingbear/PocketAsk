package com.bear.pocketask.Protocol;

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

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

}
