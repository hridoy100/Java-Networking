import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * SimpleClient.java
 * This class demonstrates a basic client that connects to a server,
 * sends a message, and receives an echo response.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client logic within a single class.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **Basic Error Handling:** Catches common I/O exceptions.
 *
 * Execution Steps:
 * 1. Compile: `javac SimpleClient.java`
 * 2. Run: `java SimpleClient`
 *    The client will attempt to connect to `localhost` on port 12345.
 *    It will send a predefined message and print the server's response.
 */
public class SimpleClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Simple Client started.");
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // 1. Connect to the server
            System.out.println("Connecting to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server.");

            // 2. Set up input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush

            // 3. Send a message to the server
            String messageToSend = "Hello from Simple Client!";
            out.println(messageToSend);
            System.out.println("Sent to server: " + messageToSend);

            // 4. Read response from server
            String serverResponse = in.readLine();
            System.out.println("Received from server: " + serverResponse);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Close resources
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
                System.out.println("Client resources closed.");
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
    }
}
