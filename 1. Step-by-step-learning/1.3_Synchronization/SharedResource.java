/**
 * SharedResource.java
 * This class represents a simple shared resource that multiple threads (ClientHandlers)
 * will attempt to access and modify. It demonstrates the need for synchronization
 * to maintain data consistency.
 *
 * Design Principles:
 * - **Encapsulation:** The shared data (`counter`) is private and accessed via methods.
 * - **Thread Safety:** Methods that modify the shared state are `synchronized` to ensure
 *   only one thread can execute them at a time, preventing race conditions.
 */
public class SharedResource {
    private int counter = 0;

    /**
     * Increments the counter in a thread-safe manner.
     * The `synchronized` keyword ensures that only one thread can execute this method at a time.
     * @return The new value of the counter.
     */
    public synchronized int incrementAndGet() {
        counter++;
        System.out.println(Thread.currentThread().getName() + " incremented counter to: " + counter);
        return counter;
    }

    /**
     * Retrieves the current value of the counter in a thread-safe manner.
     * Although reading an `int` is generally atomic, synchronizing ensures visibility
     * of the latest written value across threads (happens-before relationship).
     * @return The current value of the counter.
     */
    public synchronized int getCounter() {
        return counter;
    }

    /**
     * Resets the counter to zero in a thread-safe manner.
     */
    public synchronized void resetCounter() {
        counter = 0;
        System.out.println(Thread.currentThread().getName() + " reset counter.");
    }
}
