package io.github.hridoy100;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit; // For Thread.sleep with TimeUnit

/**
 * This class demonstrates the Producer-Consumer pattern using a {@link BlockingQueue}.
 * It sets up one producer and multiple consumer threads that interact with a shared queue.
 */
public class PCBlockingQueue {

    private static final int QUEUE_CAPACITY = 4; // The maximum number of elements the queue can hold
    private static final long SIMULATION_DURATION_SECONDS = 10; // How long the simulation should run

    public static void main(String[] args) {
        System.out.println("Producer-Consumer pattern demonstration using BlockingQueue started.");

        // Create a BlockingQueue with a fixed capacity
        BlockingQueue<String> sharedQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        System.out.println("Shared BlockingQueue created with capacity: " + QUEUE_CAPACITY);

        // Create and start producer threads
        Producer producer1 = new Producer(sharedQueue, "Producer-1");
        // If Producer's constructor starts the thread, no need to do it here.
        // If not, you would do: new Thread(producer1, "Producer-1-Thread").start();

        // Create and start consumer threads
        Consumer consumer1 = new Consumer(sharedQueue, "Consumer-1");
        Consumer consumer2 = new Consumer(sharedQueue, "Consumer-2");
        Consumer consumer3 = new Consumer(sharedQueue, "Consumer-3");
        // Similar to producer, if Consumer's constructor starts the thread, no need here.

        System.out.println("Producer and Consumer threads initialized. Running simulation for " + SIMULATION_DURATION_SECONDS + " seconds.");

        try {
            // Allow the producer and consumer threads to run for a certain duration
            TimeUnit.SECONDS.sleep(SIMULATION_DURATION_SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted during simulation.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            // Signal all threads to stop gracefully
            System.out.println("\nSimulation duration ended. Signaling threads to stop...");
            producer1.stopProducer();
            consumer1.stopConsumer();
            consumer2.stopConsumer();
            consumer3.stopConsumer();

            // Give threads a moment to finish their current tasks and stop
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Producer-Consumer pattern demonstration finished.");
        }
    }
}