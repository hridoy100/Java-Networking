import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * SimpleClient.java
 * This class demonstrates a basic client that connects to a multi-threaded server,
 * sends messages, and receives responses. It allows for interactive input from the console.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client logic within a single class.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **Basic Error Handling:** Catches common I/O exceptions.
 * - **User Interaction:** Provides a simple command-line interface for sending messages.
 *
 * Execution Steps:
 * 1. Compile: `javac SimpleClient.java`
 * 2. Run: `java SimpleClient`
 *    The client will attempt to connect to `localhost` on port 12345.
 *    You can type messages in the console, and they will be sent to the server.
 *    Type 'bye' to disconnect.
 */
public class SimpleClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Simple Client started.");
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Scanner scanner = null;

        try {
            // 1. Connect to the server
            System.out.println("Connecting to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server. Type 'bye' to exit.");

            // 2. Set up input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush
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
            String messageToSend;
            while (true) {
                System.out.print("You: ");
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
            // 5. Close resources
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
