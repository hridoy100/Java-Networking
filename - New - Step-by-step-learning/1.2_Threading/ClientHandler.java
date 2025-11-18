import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandler.java
 * This class represents a runnable task that handles communication with a single client.
 * Each connected client will have its own ClientHandler instance running in a separate thread.
 *
 * Design Principles:
 * - **Modularity:** Separates client communication logic from the main server logic.
 * - **Concurrency:** Implements `Runnable` to be executed by a `Thread` or `ExecutorService`.
 * - **Resource Management:** Ensures proper closing of client-specific sockets and streams.
 * - **Error Handling:** Catches and reports I/O exceptions during client communication.
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int clientNumber;

    /**
     * Constructor for ClientHandler.
     * @param socket The client socket connected to this handler.
     * @param clientNumber A unique identifier for this client.
     */
    public ClientHandler(Socket socket, int clientNumber) {
        this.clientSocket = socket;
        this.clientNumber = clientNumber;
        System.out.println("ClientHandler " + clientNumber + " created for " + clientSocket.getInetAddress().getHostAddress());
    }

    /**
     * The main logic for handling the client connection.
     * This method is executed when the thread starts.
     */
    @Override
    public void run() {
        try {
            // 1. Set up input and output streams for the client socket
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true); // true for auto-flush

            String clientMessage;
            // 2. Read messages from the client until the client closes the connection or sends "bye"
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client " + clientNumber + " received: " + clientMessage);

                // 3. Process the message and send a response
                String response = "Server received from Client " + clientNumber + ": " + clientMessage;
                out.println(response);

                if (clientMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client " + clientNumber + " sent 'bye'. Closing connection.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling client " + clientNumber + ": " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed stack trace
        } finally {
            // 4. Close resources specific to this client
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("Client " + clientNumber + " connection closed.");
            } catch (IOException e) {
                System.err.println("Error closing resources for client " + clientNumber + ": " + e.getMessage());
            }
        }
    }
}
