package com.bear.pocketask.Protocol;

public class NetProtocol {
    public static TRprotocol callProtocol(int function){
        TRprotocol protocol=null;
        switch (function){
            case 0:
                protocol=new RegLogProtocol();
                break;
            case 1:
                protocol=new RegLogProtocol();
                break;
            case 2:
                protocol=new CancelProtocol();
                break;
            case 3:
                protocol=new SendProtocol();
                break;
            case 4:
                protocol=new ResponseProtocol();
                break;
        }

        return protocol;
    }

    public static TRprotocol callProtocol(String intent){
        TRprotocol protocol=null;
        String []components=intent.split(TRprotocol.DIVIDE_EXTERNAL);

        if(components[0].equals("0")){
            protocol=new RegLogProtocol(components[1]);
            protocol.setFunction(0);
        }
        else if(components[0].equals("1")){
            protocol=new RegLogProtocol(components[1]);
            protocol.setFunction(1);
        }
        else if(components[0].equals("2")){
            protocol=new CancelProtocol(components[1]);
            protocol.setFunction(2);
        }
        else if(components[0].equals("3")){
            protocol=new SendProtocol(components[1]);
            protocol.setFunction(3);
        }
        else if(components[0].equals("4")){
            protocol=new ResponseProtocol(components[1]);
            protocol.setFunction(4);
        }
        
        return protocol;
    }
}
