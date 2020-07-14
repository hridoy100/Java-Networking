package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ReaderThread implements Runnable {

    ObjectInputStream ois;
    String Name;

    ReaderThread(ObjectInputStream ois, String name){
        this.ois = ois;
        this.Name = name;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                //receive from server..
                Object received = ois.readObject();
//                if(received!=null)
                    System.out.println(Name + " Got: " + (String) received);

            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
            }
        }
    }
}
