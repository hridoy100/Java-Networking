package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class represents a simple server application that listens for client connections,
 * receives messages, converts them to uppercase, and sends them back to the client.
 * It handles one client at a time without multithreading.
 * <p>
 * NOTE: In the context of the ChatApp project, this file appears to be a basic
 * client-server example and might be redundant or a precursor to the more
 * complex server logic found in {@link ServerMain}.
 * </p>
 */
public class ReaderWriterServer {

    private static final int SERVER_PORT = 22222; // The port number the server will listen on

    public static void main(String[] args) {
        System.out.println("ReaderWriterServer application started.");
        ServerSocket serverSocket = null; // Declare outside try-with-resources to close in finally

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server listening on port " + SERVER_PORT + "...");

            // Infinite loop to continuously accept new client connections
            while (true) {
                System.out.println("Waiting for a client to connect...");
                // Accept a new client connection
                // This is a blocking call; the server waits here until a client connects
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                    String receivedMessage;
                    // Loop to continuously receive messages from the connected client
                    while (true) {
                        try {
                            // Read object from client
                            Object clientObject = ois.readObject();

                            if (clientObject instanceof String) {
                                receivedMessage = (String) clientObject;
                                System.out.println("Received from client: '" + receivedMessage + "'");

                                // If client sends "exit", break the inner loop to close this client's connection
                                if ("exit".equalsIgnoreCase(receivedMessage)) {
                                    System.out.println("Client requested to exit. Closing connection.");
                                    break;
                                }

                                // Process the message (convert to uppercase)
                                String serverResponse = receivedMessage.toUpperCase();

                                // Send the processed message back to the client
                                oos.writeObject(serverResponse);
                                oos.flush(); // Ensure the message is sent immediately
                                System.out.println("Sent to client: '" + serverResponse + "'");
                            } else {
                                System.out.println("Received unexpected object type from client: " + clientObject.getClass().getName());
                                // Optionally, send an error back to the client or close connection
                                break; // Break to close connection for unexpected input
                            }
                        } catch (IOException e) {
                            // Client disconnected or other communication error
                            System.out.println("Client disconnected or communication error: " + e.getMessage());
                            break; // Break the inner loop to handle next client
                        } catch (ClassNotFoundException e) {
                            System.err.println("Error deserializing object from client: " + e.getMessage());
                            e.printStackTrace();
                            break; // Break to close connection for deserialization error
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error accepting client connection or setting up streams: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println("Client connection closed.");
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
            System.out.println("ReaderWriterServer application terminated.");
        }
    }
}