package io.github.hridoy100;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class represents a writer thread that continuously reads input from the console
 * and sends it as a {@link Data} object over a {@link NetworkConnection}.
 * It is designed to run in a separate thread to handle outgoing data without
 * blocking the main application thread.
 */
public class Writer implements Runnable {

    private final NetworkConnection netConnection; // The network connection to write to
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new Writer instance with the specified network connection.
     *
     * @param nc The {@link NetworkConnection} to which messages will be written.
     */
    public Writer(NetworkConnection nc) {
        this.netConnection = nc;
    }

    /**
     * Stops the writer thread gracefully. The {@code run} method's loop will
     * terminate after the current input operation completes.
     */
    public void stopWriterThread() {
        this.running = false;
    }

    /**
     * The main execution logic for the writer thread. It continuously reads
     * input from the console, encapsulates it in a {@link Data} object, and
     * sends it over the network connection.
     */
    @Override
    public void run() {
        System.out.println("Writer thread started.");
        // Create Scanner once outside the loop to prevent resource leaks
        try (Scanner consoleScanner = new Scanner(System.in)) {
            while (running) {
                System.out.print("Enter message to send: ");
                String messageContent = consoleScanner.nextLine();

                if ("exit".equalsIgnoreCase(messageContent)) {
                    System.out.println("Exiting writer thread.");
                    stopWriterThread();
                    // Optionally, send an "exit" message to the server
                    try {
                        netConnection.write(new Data("exit"));
                    } catch (IOException e) {
                        System.err.println("Error sending exit message: " + e.getMessage());
                    }
                    break;
                }

                Data dataToSend = new Data(messageContent);
                try {
                    // Send a clone of the Data object to ensure immutability if Data object is reused
                    netConnection.write(dataToSend.clone());
                    System.out.println("Sent: '" + messageContent + "'");
                } catch (IOException e) {
                    System.err.println("Writer thread I/O error: " + e.getMessage());
                    stopWriterThread(); // Stop the thread on I/O error
                } catch (CloneNotSupportedException e) {
                    System.err.println("Writer thread CloneNotSupportedException: " + e.getMessage());
                    e.printStackTrace();
                    stopWriterThread(); // Stop the thread on cloning error
                } catch (Exception e) {
                    System.err.println("Writer thread unexpected error: " + e.getMessage());
                    e.printStackTrace();
                    stopWriterThread(); // Stop the thread on any unexpected error
                }
            }
        } finally {
            System.out.println("Writer thread terminated.");
        }
    }
}