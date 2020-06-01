package io.github.hridoy100;

public class MainThread {
    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        System.out.println("Current Thread: "+ t);
        // change current Thread name
        t.setName("My Thread");
        System.out.println("After name change: " +t);

        for(int n=10; n>0; n--){
            System.out.println(n);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Main Thread Interrupted");
            }
        }
    }
}
