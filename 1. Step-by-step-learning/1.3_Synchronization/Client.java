import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client.java
 * This client is designed to interact with the `SynchronizedServer`.
 * It allows the user to send commands like "increment" (to increment a shared counter),
 * "get" (to retrieve the current counter value), and "bye" (to disconnect).
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client logic.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **User Interaction:** Provides a command-line interface for sending specific commands.
 * - **Concurrency:** Uses a separate thread to continuously read server responses,
 *   allowing the main thread to focus on sending user input.
 *
 * Execution Steps:
 * 1. Compile: `javac Client.java`
 * 2. Run: `java Client`
 *    The client will connect to `localhost` on port 12345.
 *    You can type commands like 'increment', 'get', or 'bye'.
 */
public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Client started.");
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Scanner scanner = null;

        try {
            System.out.println("Connecting to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server. Type 'increment', 'get', or 'bye'.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            });
            readThread.start();

            // Main thread to send messages to the server
            String command;
            while (true) {
                System.out.print("Enter command (increment, get, bye): ");
                command = scanner.nextLine();
                out.println(command);

                if (command.equalsIgnoreCase("bye")) {
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
                System.out.println("Client resources closed.");
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
    }
}
