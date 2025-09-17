package io.github.hridoy100;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * This class acts as a client handler on the server side. It implements {@link Runnable}
 * to manage communication with a single connected client in a separate thread.
 * It handles receiving messages, processing commands, and broadcasting messages
 * to other clients in the chat room.
 */
public class CreateConnection implements Runnable { // Renamed from original to reflect its role as a client handler

    private final Map<String, Information> clientList; // Reference to the server's list of connected clients
    private final NetworkConnection netConnection; // The network connection to the specific client
    private String clientUsername; // The username of this client
    private volatile boolean running = true; // Flag to control the thread's execution loop

    /**
     * Constructs a new CreateConnection (Client Handler) for a connected client.
     *
     * @param clientList A thread-safe map containing all connected clients.
     * @param netConnection The {@link NetworkConnection} established with this client.
     */
    public CreateConnection(Map<String, Information> clientList, NetworkConnection netConnection) {
        this.clientList = clientList;
        this.netConnection = netConnection;
    }

    /**
     * The main execution logic for the client handler thread.
     * It manages the lifecycle of the client connection, including
     * reading messages, processing them, and handling disconnection.
     */
    @Override
    public void run() {
        try {
            // First, get the username from the client
            // This assumes the first message from the client is their desired username
            Object initialObject = netConnection.read();
            if (initialObject instanceof String) {
                clientUsername = (String) initialObject;
                if (clientList.containsKey(clientUsername)) {
                    // Username already taken, inform client and close connection
                    netConnection.write("ERROR: Username '" + clientUsername + "' is already taken. Please try again with a different username.");
                    System.out.println("Client tried to connect with taken username: " + clientUsername);
                    return; // Terminate this handler thread
                }
                // Add client to the list
                clientList.put(clientUsername, new Information(clientUsername, netConnection));
                netConnection.write("Welcome to the chat, " + clientUsername + "!");
                broadcastMessage(clientUsername + " has joined the chat.");
                System.out.println(clientUsername + " joined from " + netConnection.getSocket().getInetAddress().getHostAddress());
            } else {
                netConnection.write("ERROR: Please send your username as the first message.");
                System.out.println("Client did not send username as first message. Disconnecting.");
                return; // Terminate this handler thread
            }

            // Main loop for continuous communication with the client
            while (running) {
                Object receivedObject = netConnection.read(); // Blocking call, waits for client message

                if (receivedObject instanceof String) {
                    String message = (String) receivedObject;
                    System.out.println(clientUsername + ": " + message);

                    // Process commands
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println(clientUsername + " requested to exit.");
                        running = false; // Signal to stop this thread
                    } else if (message.equalsIgnoreCase("list")) {
                        // Send list of connected users to this client
                        StringBuilder userList = new StringBuilder("Connected users: ");
                        clientList.keySet().forEach(user -> userList.append(user).append(", "));
                        netConnection.write(userList.substring(0, userList.length() - 2)); // Remove trailing ", "
                    } else if (message.equalsIgnoreCase("ip")) {
                        // Send client's IP address to this client
                        netConnection.write("Your IP address: " + netConnection.getSocket().getInetAddress().getHostAddress());
                    } else {
                        // Regular message, broadcast to all other clients
                        broadcastMessage(clientUsername + ": " + message);
                    }
                } else if (receivedObject != null) {
                    System.out.println("Received unexpected object type from " + clientUsername + ": " + receivedObject.getClass().getName());
                }
            }
        } catch (IOException e) {
            // Client disconnected unexpectedly or other I/O error
            if (running) { // Only log if not intentionally stopped
                System.err.println("Client handler I/O error for " + clientUsername + ": " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializing object from " + clientUsername + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up resources and remove client from list
            if (clientUsername != null) {
                clientList.remove(clientUsername);
                broadcastMessage(clientUsername + " has left the chat.");
                System.out.println(clientUsername + " disconnected.");
            }
            try {
                netConnection.close(); // Close the network connection
            } catch (IOException e) {
                System.err.println("Error closing network connection for " + clientUsername + ": " + e.getMessage());
            }
            System.out.println("Client handler thread terminated for " + (clientUsername != null ? clientUsername : "unknown client"));
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     *
     * @param message The message to broadcast.
     */
    private void broadcastMessage(String message) {
        for (Map.Entry<String, Information> entry : clientList.entrySet()) {
            if (!entry.getKey().equals(clientUsername)) { // Don't send message back to sender
                try {
                    entry.getValue().getNetConnection().write(message);
                } catch (IOException e) {
                    System.err.println("Error broadcasting message to " + entry.getKey() + ": " + e.getMessage());
                    // Optionally, remove client if connection is broken
                }
            }
        }
    }
}
