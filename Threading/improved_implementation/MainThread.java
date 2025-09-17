package io.github.hridoy100;

/**
 * This class demonstrates basic operations of the main thread in Java.
 * It shows how to get the current thread, change its name, and
 * introduce delays using {@code Thread.sleep()}.
 */
public class MainThread {

    public static void main(String[] args) {
        // Get a reference to the currently executing thread (which is the main thread)
        Thread currentThread = Thread.currentThread();

        System.out.println("Initial state of current thread: " + currentThread);

        // Change the name of the main thread for better identification in debugging
        currentThread.setName("MyMainThread");
        System.out.println("After name change: " + currentThread);

        System.out.println("Starting countdown in main thread:");
        // Loop to demonstrate a countdown with a pause
        for (int i = 10; i > 0; i--) {
            System.out.println("Countdown: " + i);
            try {
                // Pause the execution of the current thread for 500 milliseconds
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // This exception is thrown if another thread interrupts this thread while it's sleeping.
                System.out.println("Main Thread was interrupted during sleep.");
                // It's good practice to re-interrupt the current thread so that higher-level
                // code can be aware that an interruption occurred.
                Thread.currentThread().interrupt();
                // Optionally, break the loop or handle the interruption as appropriate for the application.
                break;
            }
        }
        System.out.println("Main Thread finished countdown.");
    }
}