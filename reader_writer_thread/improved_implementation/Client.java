package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents a client application that connects to a server
 * and uses separate threads for reading and writing data. This allows
 * for full-duplex communication.
 */
public class Client {

    private static final String SERVER_ADDRESS = "127.0.0.1"; // The IP address of the server
    private static final int SERVER_PORT = 22222; // The port number the server is listening on

    public static void main(String[] args) {
        System.out.println("Client application started.");
        Socket socket = null; // Declare outside try-catch to ensure it's accessible in finally
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner scanner = null; // For user input to control client lifecycle

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Successfully connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            // Create and start the writer thread
            WriterThread writerThread = new WriterThread(oos, "Client1");
            Thread writer = new Thread(writerThread, "Client1-Writer");
            writer.start();

            // Create and start the reader thread
            ReaderThread readerThread = new ReaderThread(ois, "Client1");
            Thread reader = new Thread(readerThread, "Client1-Reader");
            reader.start();

            scanner = new Scanner(System.in);
            System.out.println("Type 'exit' to disconnect from the server.");

            // Keep the main thread alive until 'exit' is typed
            while (true) {
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    System.out.println("Disconnecting from server...");
                    // Signal threads to stop
                    writerThread.stopThread();
                    readerThread.stopThread();
                    break; // Exit main loop
                }
            }

        } catch (IOException e) {
            System.err.println("Client network error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure all resources are closed
            if (scanner != null) {
                scanner.close();
            }
            try {
                if (oos != null) oos.close();
            } catch (IOException e) {
                System.err.println("Error closing ObjectOutputStream: " + e.getMessage());
            }
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                System.err.println("Error closing ObjectInputStream: " + e.getMessage());
            }
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Client socket closed.");
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
            System.out.println("Client application terminated.");
        }
    }
}
