package com.bear.pocketask.Protocol;

public abstract class TRprotocol {
    int function; //0register 1login 2cancel  3send 4response
    final static String DIVIDE_EXTERNAL="€";
    final static String DIVIDE_INTERNAL="£";

    String user;
    String password;

	String intent;

    public TRprotocol(){
        intent=function+DIVIDE_EXTERNAL;
        System.out.println(intent);
    }

    public void setFunction(int function){
    	this.function=function;
    }
    public int getFunction() {
		return function;
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
    
    public abstract String getPassword();

	public abstract void setPassword(String password);
    
}
