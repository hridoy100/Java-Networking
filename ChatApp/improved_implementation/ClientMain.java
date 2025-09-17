package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class serves as the main entry point for the client application in the ChatApp project.
 * It establishes a connection to the chat server, sends messages, and receives responses.
 * This client is designed for basic interaction and demonstrates fundamental socket communication.
 */
public class ClientMain {

    private static final String SERVER_ADDRESS = "127.0.0.1"; // The IP address of the chat server
    private static final int SERVER_PORT = 22222; // The port number the chat server is listening on

    public static void main(String[] args) {
        System.out.println("Chat Client application started.");

        // Using try-with-resources to ensure all network and I/O resources are properly closed
        try (
            Scanner consoleScanner = new Scanner(System.in); // Scanner for reading user input from the console
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Establish connection to the server
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // Stream to send objects to server
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()) // Stream to receive objects from server
        ) {
            System.out.println("Successfully connected to chat server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            System.out.println("Type your messages and press Enter. Type 'exit' to disconnect.");

            String messageToSend;
            // Loop to continuously send messages to the server and receive responses
            while (true) {
                System.out.print("You: "); // Prompt for user input
                messageToSend = consoleScanner.nextLine();

                if ("exit".equalsIgnoreCase(messageToSend)) {
                    System.out.println("Disconnecting from chat server...");
                    break; // Exit the loop and close the connection
                }

                // Send the user's message to the server
                oos.writeObject(messageToSend);
                oos.flush(); // Ensure data is sent immediately

                // Attempt to read a response from the server
                Object receivedObject = ois.readObject();
                if (receivedObject instanceof String) {
                    System.out.println("Server: " + (String) receivedObject);
                } else {
                    // Handle unexpected object types from the server
                    System.out.println("Server sent an unexpected object type: " + receivedObject.getClass().getName());
                }
            }

        } catch (IOException e) {
            // Catch and log network-related errors (e.g., server not found, connection reset)
            System.err.println("Network error in Chat Client: " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed stack trace during debugging
        } catch (ClassNotFoundException e) {
            // Catch and log errors if a received object's class definition is not found
            System.err.println("Error deserializing object from server: " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed stack trace during debugging
        } catch (Exception e) {
            // Catch any other unexpected runtime exceptions
            System.err.println("An unexpected error occurred in Chat Client: " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed stack trace during debugging
        } finally {
            System.out.println("Chat Client application terminated.");
        }
    }
}