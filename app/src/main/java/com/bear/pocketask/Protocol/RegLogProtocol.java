package com.bear.pocketask.Protocol;

public class RegLogProtocol extends TRprotocol {
    String password;

    public RegLogProtocol() {
        super();
    }

    public RegLogProtocol(String content){
        String []contents=content.split(TRprotocol.DIVIDE_INTERNAL);
        user=contents[0];
        password=contents[1];
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIntent(){
        intent+=user+DIVIDE_INTERNAL+password;
        System.out.println("hhaha"+intent);
    }

}
