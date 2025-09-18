import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * ChatClient.java
 * This class implements a client for the multi-user chat application. It connects to the
 * `ChatServer`, allows the user to enter a name, send messages, and receive messages
 * from other participants in the chat.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client-side chat logic.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **User Interaction:** Provides a command-line interface for chat.
 * - **Concurrency:** Uses a separate thread to continuously read server messages,
 *   allowing the main thread to handle user input for sending messages.
 * - **Robustness:** Handles server disconnections and I/O errors.
 *
 * Execution Steps:
 * 1. Compile: `javac ChatClient.java`
 * 2. Run: `java ChatClient`
 *    The client will connect to `localhost` on port 12345.
 *    You will be prompted to enter your name, then you can start sending messages.
 *    Type 'bye' to leave the chat.
 */
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Chat Client started.");
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Scanner scanner = null;

        try {
            System.out.println("Connecting to chat server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to chat server.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Disconnected from server or error reading: " + e.getMessage());
                }
            });
            readThread.start();

            // Main thread to send messages to the server
            String messageToSend;
            while (true) {
                messageToSend = scanner.nextLine();
                out.println(messageToSend);

                if (messageToSend.equalsIgnoreCase("bye")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
                if (scanner != null) scanner.close();
                System.out.println("Client resources closed. Disconnected from chat.");
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
    }
}
