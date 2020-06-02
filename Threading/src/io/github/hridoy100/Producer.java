package io.github.hridoy100;

import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {

    BlockingQueue<String> q;
    Thread t;
    String name;

    Producer(BlockingQueue<String> q, String name){
        this.q = q;
        this.name = name;
        t = new Thread(this, name);
        t.start();
    }

    @Override
    public void run() {
        System.out.println(name+ "started...");
        int i=0;
        while (true) {
            try {

                if(q.size()>=4){
                    System.out.println(name+ ": Queue is full...");
                    Thread.sleep(2000);
                }
                i++;
                q.put("cake"+i);
                System.out.println(name + " created cake" + i);

                Thread.sleep(300);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

