package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress; // Added for getting local host address

/**
 * This class represents the main server application for the Reader-Writer Thread example.
 * It listens for incoming client connections and, for each connection, spawns a new
 * {@link ServerThread} to handle communication with that client concurrently.
 */
public class Server {

    private static final int SERVER_PORT = 22222; // The port number the server will listen on

    public static void main(String[] args) {
        System.out.println("Server application started.");
        ServerSocket serverSocket = null; // Declare outside try-catch to ensure it's accessible in finally

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server listening on port " + SERVER_PORT + "...");
            System.out.println("Server IP Address: " + InetAddress.getLocalHost().getHostAddress());

            // Infinite loop to continuously accept new client connections
            while (true) {
                System.out.println("Waiting for a client to connect...");
                // Accept a new client connection. This is a blocking call.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                // Spawn a new ServerThread to handle this client connection
                new ServerThread(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the server socket is closed when the server application terminates
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                } catch (IOException e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Server application terminated.");
        }
    }
}

/**
 * This class represents a dedicated thread for handling communication with a single client.
 * It reads objects from the client, processes them (converts to uppercase), and sends
 * the response back to the client.
 */
class ServerThread implements Runnable {

    private final Socket clientSocket; // The socket connected to the client
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new ServerThread to handle communication with a specific client.
     * A new {@link Thread} is created and started upon construction.
     *
     * @param clientSocket The {@link Socket} connected to the client.
     */
    ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        // Start the thread immediately upon construction
        new Thread(this, "ClientHandler-" + clientSocket.getInetAddress().getHostAddress()).start();
    }

    /**
     * Signals the thread to stop its execution gracefully.
     */
    public void stopThread() {
        this.running = false;
    }

    /**
     * The main execution logic for the server thread. It continuously reads
     * objects from the client, processes them, and sends responses.
     */
    @Override
    public void run() {
        System.out.println("ServerThread started for client: " + clientSocket.getInetAddress().getHostAddress());

        // Use try-with-resources to ensure streams and socket are closed automatically
        try (
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            while (running) {
                Object receivedObject = ois.readObject(); // Blocking call, waits for client message

                if (receivedObject == null) {
                    System.out.println("Client disconnected gracefully (received null object).");
                    break; // Exit loop if client sends null or disconnects
                }

                if (receivedObject instanceof String) {
                    String clientMessage = (String) receivedObject;
                    System.out.println("From Client " + clientSocket.getInetAddress().getHostAddress() + ": " + clientMessage);

                    // Process the message (convert to uppercase)
                    String serverResponse = clientMessage.toUpperCase();

                    // Send the processed message back to the client
                    oos.writeObject(serverResponse);
                    oos.flush(); // Ensure the message is sent immediately
                    System.out.println("Sent to Client " + clientSocket.getInetAddress().getHostAddress() + ": " + serverResponse);
                } else {
                    System.out.println("Received unexpected object type from client: " + receivedObject.getClass().getName());
                }
            }
        } catch (IOException e) {
            // This often means the client disconnected unexpectedly or an I/O error occurred
            if (running) { // Only log if the thread was not intentionally stopped
                System.err.println("ServerThread I/O error for client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ServerThread ClassNotFoundException for client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ServerThread unexpected error for client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the client socket is closed
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                    System.out.println("Client socket closed for " + clientSocket.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    System.err.println("Error closing client socket for " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
                }
            }
            System.out.println("ServerThread terminated for client: " + clientSocket.getInetAddress().getHostAddress());
        }
    }
}
