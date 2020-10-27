/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hridoy100;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class ReaderWriterServer implements Runnable {

    String username;
    NetworkConnection nc;
    HashMap<String, Information> clientList;

    public ReaderWriterServer(String user, NetworkConnection netConnection, HashMap<String, Information> cList) {
        username = user;
        nc = netConnection;
        clientList = cList;
    }

    @Override
    public void run() {
        while (true) {
            Object obj = nc.read();
            Data dataObj=(Data)obj;
            String actualMessage=dataObj.message;
            System.out.println(actualMessage);
            if (actualMessage.toLowerCase().contains("list")) {
                System.out.println("List asked.." + actualMessage);
                String words[] = actualMessage.split("\\$");
                /*
                words[0] = Sender Name
                words[1] = Receiver Name
                words[2] = keyword
                words[3] = message/null
                */
                System.out.println("Client List: \n" + clientList);
                Information info = clientList.get(words[0]);
                String msgToSend = new String("List of Clients...\n");
                for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                    String key = entry.getKey();
                    //Information value = entry.getValue();
                    msgToSend = new String(msgToSend + key + "\n");
                    //System.out.println(key);
                }
                Object object = msgToSend;
                System.out.println("sending.." + msgToSend);
                System.out.println("words0: " + words[0]);
                info.netConnection.write(msgToSend);
                //String messageToSend=username+" -> "+sendMsg;
                //Data data=new Data();
                //data.message=messageToSend;
            }
            if (actualMessage.toLowerCase().contains("ip")){
                String words[] = actualMessage.split("\\$");
                /*
                words[0] = Sender Name
                words[1] = Receiver Name
                words[2] = keyword = ip
                words[3] = message/null
                */
                System.out.println("Client List: \n" + clientList);
                Information info = clientList.get(words[0]);
                String msgToSend = new String("Your PORT: \n");
                msgToSend+=info.netConnection.getSocket().getLocalAddress().getHostAddress();
                Object object = msgToSend;
                System.out.println("sending.." + msgToSend);
                System.out.println("words0: " + words[0]);
                info.netConnection.write(msgToSend);
            }
            if (actualMessage.toLowerCase().contains("send")){
                String words[] = actualMessage.split("\\$");
                /*
                words[0] = Sender Name
                words[1] = Receiver Name
                words[2] = keyword = send
                words[3] = message
                */
                Information info = clientList.get(words[1]);
                String msgToSend = words[0]+" says: " + words[3];
                Object object = msgToSend;
                System.out.println("sending.." + msgToSend);
                System.out.println("words0: " + words[0]);
                info.netConnection.write(msgToSend);
            }
        }

    }

}
