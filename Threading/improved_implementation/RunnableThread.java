package io.github.hridoy100;

/**
 * This class demonstrates how to create and run a thread by implementing the {@link Runnable} interface.
 * It defines a task that a thread will execute.
 */
class ChildRunnableThread implements Runnable {

    private final int threadNumber; // A unique identifier for this child thread
    private final Thread thread; // The actual Thread object
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new ChildRunnableThread.
     *
     * @param threadNumber A unique number to identify this thread.
     */
    ChildRunnableThread(int threadNumber) {
        this.threadNumber = threadNumber;
        this.thread = new Thread(this, "ChildThread-" + threadNumber);
        this.thread.start();
    }

    /**
     * Returns the {@link Thread} object associated with this runnable.
     *
     * @return The {@link Thread} object.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Signals the thread to stop its execution gracefully.
     */
    public void stopThread() {
        this.running = false;
        this.thread.interrupt(); // Interrupt the thread to unblock it if it's sleeping
    }

    /**
     * The main execution logic for the child thread. It performs a simple countdown.
     */
    @Override
    public void run() {
        System.out.println("Child Thread " + threadNumber + " started.");
        for (int i = 0; i < 5 && running; i++) {
            System.out.println("Child Thread " + threadNumber + ": " + i);
            try {
                Thread.sleep(500); // Simulate some work
            } catch (InterruptedException e) {
                System.out.println("Child Thread " + threadNumber + " was interrupted.");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                running = false; // Stop the loop
            }
        }
        System.out.println("Child Thread " + threadNumber + " exiting.");
    }
}

/**
 * This class demonstrates the creation and management of multiple threads
 * using the {@link Runnable} interface. It creates and starts two child threads,
 * waits for their completion using {@code join()}, and then reports their status.
 */
public class RunnableThread {
    public static void main(String[] args) {
        System.out.println("Main Thread Started...");

        // Create and start two child threads
        ChildRunnableThread ob1 = new ChildRunnableThread(1);
        ChildRunnableThread ob2 = new ChildRunnableThread(2);

        System.out.println("Thread 1 is alive: " + ob1.getThread().isAlive());
        System.out.println("Thread 2 is alive: " + ob2.getThread().isAlive());

        try {
            // Wait for both child threads to complete their execution
            System.out.println("Waiting for child threads to finish...");
            ob1.getThread().join();
            ob2.getThread().join();
        } catch (InterruptedException e) {
            System.err.println("Main Thread interrupted while waiting for child threads.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        System.out.println("Thread 1 is alive: " + ob1.getThread().isAlive());
        System.out.println("Thread 2 is alive: " + ob2.getThread().isAlive());

        System.out.println("Main Thread Exited...");
    }
}