package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * This class represents a dedicated thread for writing objects to an {@link ObjectOutputStream}.
 * It continuously reads input from the console and sends it as an object over the stream.
 * Designed to run in parallel with a {@link ReaderThread} for full-duplex communication.
 */
public class WriterThread implements Runnable {

    private final ObjectOutputStream oos; // The output stream to write objects to
    private final String clientName; // A name or identifier for the client associated with this writer
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new WriterThread.
     *
     * @param oos The {@link ObjectOutputStream} to which to write objects.
     * @param name A descriptive name for the client or connection this writer is serving.
     */
    public WriterThread(ObjectOutputStream oos, String name) {
        this.oos = oos;
        this.clientName = name;
        // It's generally better to have the caller start the thread, but keeping original behavior for now.
        // A more robust design would separate thread creation/start from the constructor.
        new Thread(this, clientName + "-Writer").start();
    }

    /**
     * Signals the thread to stop its execution gracefully.
     * The {@code run} method's loop will terminate after the current input operation completes.
     */
    public void stopThread() {
        this.running = false;
    }

    /**
     * The main execution logic for the writer thread. It continuously reads
     * input from the console and writes it as a String object to the output stream.
     */
    @Override
    public void run() {
        System.out.println("Writer thread '" + clientName + "-Writer' started. Type 'exit' to stop.");
        // Create Scanner once outside the loop to prevent resource leaks
        try (Scanner inputScanner = new Scanner(System.in)) {
            while (running) {
                System.out.print(clientName + " (send): ");
                String message = inputScanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Writer thread '" + clientName + "-Writer' exiting.");
                    stopThread(); // Signal to stop the thread
                    break; // Exit the loop
                }

                try {
                    oos.writeObject(message);
                    oos.flush(); // Ensure the message is sent immediately
                    System.out.println("Message Sent: '" + message + "'");
                } catch (IOException e) {
                    // This often means the connection has been closed by the other end
                    if (running) { // Only log if the thread was not intentionally stopped
                        System.err.println("WriterThread '" + clientName + "-Writer' I/O error: " + e.getMessage());
                    }
                    stopThread(); // Stop thread on I/O error
                } catch (Exception e) {
                    System.err.println("WriterThread '" + clientName + "-Writer' unexpected error: " + e.getMessage());
                    e.printStackTrace();
                    stopThread(); // Stop thread on any unexpected error
                }
            }
        } finally {
            System.out.println("Writer thread '" + clientName + "-Writer' stopped.");
        }
    }
}