import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandlerSync.java
 * This class is a specialized ClientHandler that interacts with a shared resource.
 * It demonstrates how multiple client threads can safely access and modify a shared object
 * by relying on the synchronization mechanisms implemented within the `SharedResource` class.
 *
 * Design Principles:
 * - **Modularity:** Handles client-specific communication logic.
 * - **Concurrency:** Implements `Runnable` to be executed by a thread pool.
 * - **Resource Management:** Ensures proper closing of client-specific sockets and streams.
 * - **Interaction with Shared State:** Passes a `SharedResource` instance to allow clients
 *   to interact with a common data source, showcasing synchronization in action.
 */
public class ClientHandlerSync implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int clientNumber;
    private SharedResource sharedResource;

    /**
     * Constructor for ClientHandlerSync.
     * @param socket The client socket connected to this handler.
     * @param clientNumber A unique identifier for this client.
     * @param sharedResource The shared resource instance that this handler will interact with.
     */
    public ClientHandlerSync(Socket socket, int clientNumber, SharedResource sharedResource) {
        this.clientSocket = socket;
        this.clientNumber = clientNumber;
        this.sharedResource = sharedResource;
        System.out.println("ClientHandlerSync " + clientNumber + " created.");
    }

    /**
     * The main logic for handling the client connection.
     * This method is executed when the thread starts.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client " + clientNumber + " received: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("increment")) {
                    int newValue = sharedResource.incrementAndGet();
                    out.println("Server: Counter incremented to " + newValue);
                } else if (clientMessage.equalsIgnoreCase("get")) {
                    int currentValue = sharedResource.getCounter();
                    out.println("Server: Current counter value is " + currentValue);
                } else if (clientMessage.equalsIgnoreCase("bye")) {
                    out.println("Server: Goodbye!");
                    System.out.println("Client " + clientNumber + " sent 'bye'. Closing connection.");
                    break;
                } else {
                    out.println("Server: Unknown command. Try 'increment', 'get', or 'bye'.");
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling client " + clientNumber + ": " + e.getMessage());
        } finally {
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
