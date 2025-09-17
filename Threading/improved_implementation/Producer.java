package io.github.hridoy100;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit; // For Thread.sleep with TimeUnit

/**
 * This class represents a producer in a Producer-Consumer pattern.
 * It continuously generates elements and puts them into a {@link BlockingQueue}.
 * Each producer runs in its own thread.
 */
public class Producer implements Runnable {

    private final BlockingQueue<String> queue; // The shared queue to which to produce elements
    private final String name; // The name of this producer thread
    private volatile boolean running = true; // Flag to control the thread's execution loop
    private int itemCounter = 0; // Counter for the items produced

    /**
     * Constructs a new Producer.
     *
     * @param q The {@link BlockingQueue} to produce to.
     * @param name A descriptive name for this producer.
     */
    public Producer(BlockingQueue<String> q, String name) {
        this.queue = q;
        this.name = name;
        // It's generally better to have the caller start the thread, but keeping original behavior for now.
        new Thread(this, name).start();
    }

    /**
     * Signals the producer thread to stop its execution gracefully.
     * The {@code run} method's loop will terminate after the current operation completes.
     */
    public void stopProducer() {
        this.running = false;
        // Interrupt the thread to unblock it if it's currently waiting on queue.put()
        // This assumes the thread that calls stopProducer() has a reference to the Thread object.
        // For this example, we'll rely on the 'running' flag.
    }

    /**
     * The main execution logic for the producer thread. It continuously
     * generates items and puts them into the queue.
     */
    @Override
    public void run() {
        System.out.println(name + " started...");
        while (running) {
            try {
                itemCounter++;
                String item = "cake-" + itemCounter;
                // put() is a blocking operation; it waits until space is available in the queue
                queue.put(item);
                System.out.println(name + " produced: " + item);

                // Simulate work being done before producing the next item
                TimeUnit.MILLISECONDS.sleep(300);

            } catch (InterruptedException e) {
                // This exception is thrown if the thread is interrupted while waiting
                System.err.println(name + " was interrupted while waiting to produce. Shutting down.");
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