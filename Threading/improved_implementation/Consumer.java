package io.github.hridoy100;

import java.util.concurrent.BlockingQueue;

/**
 * This class represents a consumer in a Producer-Consumer pattern.
 * It continuously takes elements from a {@link BlockingQueue} and processes them.
 * Each consumer runs in its own thread.
 */
public class Consumer implements Runnable {

    private final BlockingQueue<String> queue; // The shared queue from which to consume elements
    private final String name; // The name of this consumer thread
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new Consumer.
     *
     * @param q The {@link BlockingQueue} to consume from.
     * @param name A descriptive name for this consumer.
     */
    public Consumer(BlockingQueue<String> q, String name) {
        this.queue = q;
        this.name = name;
        // It's generally better to have the caller start the thread, but keeping original behavior for now.
        new Thread(this, name).start();
    }

    /**
     * Signals the consumer thread to stop its execution gracefully.
     * The {@code run} method's loop will terminate after the current operation completes.
     */
    public void stopConsumer() {
        this.running = false;
        // Interrupt the thread to unblock it if it's currently waiting on queue.take()
        // This assumes the thread that calls stopConsumer() has a reference to the Thread object.
        // For this example, we'll rely on the 'running' flag.
    }

    /**
     * The main execution logic for the consumer thread. It continuously
     * takes elements from the queue and prints them.
     */
    @Override
    public void run() {
        System.out.println(name + " started...");
        while (running) {
            try {
                // take() is a blocking operation; it waits until an element is available
                String item = queue.take();
                System.out.println(name + " consumed: " + item);
                Thread.sleep(1000); // Simulate processing time
            } catch (InterruptedException e) {
                // This exception is thrown if the thread is interrupted while waiting
                System.err.println(name + " was interrupted while waiting to consume. Shutting down.");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                running = false; // Stop the loop
            } catch (Exception e) {
                System.err.println(name + " encountered an unexpected error: " + e.getMessage());
                e.printStackTrace();
                running = false; // Stop the loop on other errors
            }
        }
        System.out.println(name + " stopped.");
    }
}