package io.github.hridoy100;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the main server application for the ChatApp.
 * It listens for incoming client connections, accepts them, and then
 * delegates the handling of each client to a separate thread.
 * It maintains a list of all connected clients.
 */
public class ServerMain {

    private static final int SERVER_PORT = 12345; // The port number the server will listen on
    // A thread-safe map to store connected clients, mapping username to their Information object
    private static final Map<String, Information> clientList = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        System.out.println("Chat Server application started.");
        ServerSocket serverSocket = null; // Declare outside try-with-resources to close in finally

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server listening on port " + SERVER_PORT + "...");
            System.out.println("Server IP Address: " + InetAddress.getLocalHost().getHostAddress());

            // Infinite loop to continuously accept new client connections
            while (true) {
                System.out.println("Waiting for a client to connect...");
                // Accept a new client connection
                // This is a blocking call; the server waits here until a client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                // Create a NetworkConnection for the new client
                NetworkConnection nc = new NetworkConnection(clientSocket);

                // Create a new thread to handle communication with this client
                // The ClientHandler (or a re-purposed CreateConnection) will manage
                // the client's interaction and add/remove them from clientList.
                // For now, assuming CreateConnection is adapted to be a Runnable client handler.
                Thread clientHandlerThread = new Thread(new CreateConnection(clientList, nc),
                                                        "ClientHandler-" + clientSocket.getInetAddress().getHostAddress());
                clientHandlerThread.start();
            }

        } catch (IOException e) {
            // Handle errors related to server socket creation or acceptance
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
            System.out.println("Chat Server application terminated.");
        }
    }

    /**
     * Returns the thread-safe map of connected clients.
     *
     * @return A map where keys are usernames and values are {@link Information} objects.
     */
    public static Map<String, Information> getClientList() {
        return clientList;
    }
}