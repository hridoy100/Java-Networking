package io.github.hridoy100;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PCBlockingQueue {
    public static void main(String[] args) {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(4);
        Producer producer1 = new Producer(q, "Producer1");
        Consumer consumer1 = new Consumer(q, "Consumer1");
        Consumer consumer2 = new Consumer(q, "Consumer2");
        Consumer consumer3 = new Consumer(q, "Consumer3");
    }
}
