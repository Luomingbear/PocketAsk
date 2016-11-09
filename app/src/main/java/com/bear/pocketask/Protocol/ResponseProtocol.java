package com.bear.pocketask.Protocol;

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
