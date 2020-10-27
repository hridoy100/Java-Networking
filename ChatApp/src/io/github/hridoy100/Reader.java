/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hridoy100;

/**
 *
 * @author user
 */
public class Reader implements Runnable{
    public NetworkConnection netConnection;
    String msg="";
    public static boolean check = false;
    public Reader(NetworkConnection nc){
        netConnection=nc;
    }
    public void setMessage(String msg){
        this.msg=msg;
    }
    public void StartThread(){
        new Thread(this).start();
        //this.start();
    }
    public String getMessage() {
        if(msg!=null)
        return msg;
        else return "";
    }
    
    
    @Override
    public void run() {
        while(true){
            String msg;
            Object obj=netConnection.read();
            msg = (String) obj;
            //Data dataObj=(Data)obj;
            if(!getMessage().equals(msg));
            {
                setMessage(msg);
                System.out.println("Received : "+msg);
                check = false;
            }
//            if(check==true){
//            setMessage(msg);
//            System.out.println("Received : "+msg);
//            check = false;
//            }
            
        }
    }
    
}
