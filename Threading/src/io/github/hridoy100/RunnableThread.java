package io.github.hridoy100;

class NewThread implements Runnable{

    Thread t;
    int threadNo;

    NewThread(int threadNo) {
        t = new Thread(this, "Runnable Thread 1");
        this.threadNo = threadNo;
        t.start();
    }

    @Override
    public void run() {
        for(int i=0; i<5; i++){
            System.out.println( threadNo+ "-Child Thread: "+ i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(threadNo+ "-Child Thread Interrupted");
            }
        }
        System.out.println("Exiting Child Thread...");
    }
}

public class RunnableThread {
    public static void main(String[] args) {
        System.out.println("Main Thread Started...");
        NewThread ob1 = new NewThread(1);
        NewThread ob2 = new NewThread(2);
        System.out.println("Thread 1 is alive: "+ob1.t.isAlive());
        System.out.println("Thread 2 is alive: "+ob2.t.isAlive());

        try {
            ob1.t.join();
            ob2.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread 1 is alive: "+ob1.t.isAlive());
        System.out.println("Thread 2 is alive: "+ob2.t.isAlive());

        System.out.println("Main Thread Exited...");
    }
}
