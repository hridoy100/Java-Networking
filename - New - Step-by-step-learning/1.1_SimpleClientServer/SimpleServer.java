import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SimpleServer.java
 * This class demonstrates a basic server that listens for client connections,
 * receives a message, and sends an echo response back to the client.
 * It handles only one client connection at a time.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates server logic within a single class.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **Basic Error Handling:** Catches common I/O exceptions.
 *
 * Execution Steps:
 * 1. Compile: `javac SimpleServer.java`
 * 2. Run: `java SimpleServer`
 *    The server will start and listen on port 12345.
 *    It will print messages to the console indicating connection status and received/sent data.
 */
public class SimpleServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Simple Server started. Listening on port " + PORT);
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // 1. Create a ServerSocket to listen for incoming connections
            serverSocket = new ServerSocket(PORT);

            // 2. Wait for a client to connect
            System.out.println("Waiting for a client to connect...");
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // 3. Set up input and output streams
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true); // true for auto-flush

            // 4. Read message from client
            String clientMessage = in.readLine();
            System.out.println("Received from client: " + clientMessage);

            // 5. Send echo response to client
            String echoMessage = "Server received: " + clientMessage;
            out.println(echoMessage);
            System.out.println("Sent to client: " + echoMessage);

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Close resources
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                if (serverSocket != null) serverSocket.close();
                System.out.println("Server resources closed.");
            } catch (IOException e) {
                System.err.println("Error closing server resources: " + e.getMessage());
            }
        }
    }
}
