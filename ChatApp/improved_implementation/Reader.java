package io.github.hridoy100;

import java.io.IOException;

/**
 * This class represents a reader thread that continuously reads objects (messages)
 * from a {@link NetworkConnection}. It is designed to run in a separate thread
 * to handle incoming data without blocking the main application thread.
 */
public class Reader implements Runnable {

    private final NetworkConnection netConnection; // The network connection to read from
    private volatile String receivedMessage = ""; // Stores the last received message, volatile for thread safety
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new Reader instance with the specified network connection.
     *
     * @param nc The {@link NetworkConnection} from which to read messages.
     */
    public Reader(NetworkConnection nc) {
        this.netConnection = nc;
    }

    /**
     * Sets the received message. This method is primarily for internal use
     * or if an external component needs to update the message (though typically
     * the reader thread itself updates it).
     *
     * @param msg The message string to set.
     */
    private synchronized void setReceivedMessage(String msg) {
        this.receivedMessage = msg;
    }

    /**
     * Returns the last received message.
     *
     * @return The last message received, or an empty string if no message has been received yet.
     */
    public synchronized String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * Starts the reader thread. This method creates a new {@link Thread} and
     * begins its execution.
     */
    public void startReaderThread() {
        new Thread(this, "ReaderThread-" + netConnection.getSocket().getInetAddress().getHostAddress()).start();
    }

    /**
     * Stops the reader thread gracefully. The {@code run} method's loop will
     * terminate after the current read operation completes.
     */
    public void stopReaderThread() {
        this.running = false;
    }

    /**
     * The main execution logic for the reader thread. It continuously reads
     * objects from the network connection, updates the received message, and
     * prints it to the console.
     */
    @Override
    public void run() {
        System.out.println("Reader thread started for connection: " + netConnection.getSocket().getInetAddress().getHostAddress());
        while (running) {
            try {
                Object obj = netConnection.read(); // Attempt to read an object from the stream

                if (obj instanceof String) {
                    String msg = (String) obj;
                    // Only update and print if the message is new or different from the last one
                    // This prevents redundant output if the server sends the same message repeatedly
                    if (!getReceivedMessage().equals(msg)) {
                        setReceivedMessage(msg);
                        System.out.println("Received: " + msg);
                    }
                } else if (obj != null) {
                    System.out.println("Received unexpected object type: " + obj.getClass().getName());
                }
            } catch (IOException e) {
                // This often means the connection has been closed by the other end
                if (running) { // Only log if the thread was not intentionally stopped
                    System.err.println("Reader thread I/O error: " + e.getMessage());
                }
                stopReaderThread(); // Stop the thread on I/O error
            } catch (ClassNotFoundException e) {
                // This means a serialized object's class could not be found
                System.err.println("Reader thread ClassNotFoundException: " + e.getMessage());
                e.printStackTrace();
                stopReaderThread(); // Stop the thread on deserialization error
            } catch (Exception e) {
                // Catch any other unexpected exceptions
                System.err.println("Reader thread unexpected error: " + e.getMessage());
                e.printStackTrace();
                stopReaderThread(); // Stop the thread on any unexpected error
            }
        }
        System.out.println("Reader thread stopped for connection: " + netConnection.getSocket().getInetAddress().getHostAddress());
    }
}