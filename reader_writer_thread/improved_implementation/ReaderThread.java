package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * This class represents a dedicated thread for reading objects from an {@link ObjectInputStream}.
 * It continuously listens for incoming objects and prints them to the console.
 * Designed to run in parallel with a {@link WriterThread} for full-duplex communication.
 */
public class ReaderThread implements Runnable {

    private final ObjectInputStream ois; // The input stream to read objects from
    private final String clientName; // A name or identifier for the client associated with this reader
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new ReaderThread.
     *
     * @param ois The {@link ObjectInputStream} from which to read objects.
     * @param name A descriptive name for the client or connection this reader is serving.
     */
    public ReaderThread(ObjectInputStream ois, String name) {
        this.ois = ois;
        this.clientName = name;
        // It's generally better to have the caller start the thread, but keeping original behavior for now.
        // A more robust design would separate thread creation/start from the constructor.
        new Thread(this, clientName + "-Reader").start();
    }

    /**
     * Signals the thread to stop its execution gracefully.
     * The {@code run} method's loop will terminate after the current read operation completes.
     */
    public void stopThread() {
        this.running = false;
    }

    /**
     * The main execution logic for the reader thread. It continuously reads
     * objects from the input stream and prints them to the console.
     */
    @Override
    public void run() {
        System.out.println("Reader thread '" + clientName + "-Reader' started.");
        while (running) {
            try {
                Object received = ois.readObject();
                if (received != null) {
                    System.out.println(clientName + " received: " + received);
                }
            } catch (ClassNotFoundException e) {
                System.err.println("ReaderThread '" + clientName + "-Reader' ClassNotFoundException: " + e.getMessage());
                e.printStackTrace();
                stopThread(); // Stop thread on deserialization error
            } catch (IOException e) {
                // This often means the connection has been closed by the other end
                if (running) { // Only log if the thread was not intentionally stopped
                    System.err.println("ReaderThread '" + clientName + "-Reader' I/O error: " + e.getMessage());
                }
                stopThread(); // Stop thread on I/O error
            } catch (Exception e) {
                System.err.println("ReaderThread '" + clientName + "-Reader' unexpected error: " + e.getMessage());
                e.printStackTrace();
                stopThread(); // Stop thread on any unexpected error
            }
        }
        System.out.println("Reader thread '" + clientName + "-Reader' stopped.");
    }
}