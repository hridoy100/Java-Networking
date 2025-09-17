package io.github.hridoy100;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents a simple client application that connects to a server,
 * sends messages, and receives responses. It demonstrates basic socket
 * programming with continuous communication until an "exit" command is given.
 */
public class Client {

    private static final String SERVER_ADDRESS = "127.0.0.1"; // The IP address of the server
    private static final int SERVER_PORT = 22222; // The port number the server is listening on

    public static void main(String[] args) {
        System.out.println("Client application started.");

        // Use try-with-resources to ensure all resources are closed automatically
        try (
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Successfully connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            System.out.println("Type your messages and press Enter. Type 'exit' to quit.");

            String messageToSend;
            // Loop to continuously send messages until the user types "exit"
            while (true) {
                System.out.print("You: ");
                messageToSend = scanner.nextLine();

                if ("exit".equalsIgnoreCase(messageToSend)) {
                    System.out.println("Exiting client application.");
                    break; // Exit the loop if user types 'exit'
                }

                // Send the message to the server
                oos.writeObject(messageToSend);
                oos.flush(); // Ensure the message is sent immediately
                System.out.println("Sent: '" + messageToSend + "' to server.");

                // Receive and display the response from the server
                Object fromServer = ois.readObject();
                if (fromServer instanceof String) {
                    System.out.println("Received from server: '" + (String) fromServer + "'");
                } else {
                    System.out.println("Received unexpected object from server: " + fromServer.getClass().getName());
                }
            }

        } catch (IOException e) {
            // Handle network-related errors (e.g., server not running, connection reset)
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Handle errors if the received object's class cannot be found
            System.err.println("Error receiving object from server: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Client application terminated.");
        }
    }
}