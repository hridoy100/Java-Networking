import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * ClientHandlerChat.java
 * This class handles communication with a single chat client. It reads messages from its client
 * and broadcasts them to all other connected clients in the chat room.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client-specific chat logic.
 * - **Concurrency:** Implements `Runnable` to allow each client to be handled in a separate thread.
 * - **Resource Management:** Ensures proper closing of client-specific sockets and streams.
 * - **Broadcasting:** Coordinates with the `ChatServer` to send messages to all participants.
 * - **Robustness:** Handles client disconnections gracefully.
 */
public class ClientHandlerChat implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private List<ClientHandlerChat> clientHandlers; // Reference to all connected clients

    /**
     * Constructor for ClientHandlerChat.
     * @param socket The client socket connected to this handler.
     * @param clientHandlers A list of all active client handlers in the chat server.
     */
    public ClientHandlerChat(Socket socket, List<ClientHandlerChat> clientHandlers) {
        this.clientSocket = socket;
        this.clientHandlers = clientHandlers;
    }

    /**
     * Sends a message to this specific client.
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
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

            // 1. Ask client for their name and set it
            out.println("SERVER: Enter your name:");
            clientName = in.readLine();
            if (clientName == null || clientName.trim().isEmpty()) {
                clientName = "Guest" + clientSocket.getPort(); // Fallback name
            }
            System.out.println(clientName + " has joined the chat.");
            broadcastMessage("SERVER: " + clientName + " has joined the chat.");

            // 2. Add this handler to the list of active handlers
            synchronized (clientHandlers) {
                clientHandlers.add(this);
            }

            String clientMessage;
            // 3. Read messages from the client and broadcast them
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("bye")) {
                    break;
                }
                System.out.println(clientName + ": " + clientMessage);
                broadcastMessage(clientName + ": " + clientMessage);
            }

        } catch (IOException e) {
            System.err.println("Error handling client " + clientName + ": " + e.getMessage());
        } finally {
            // 4. Clean up resources and remove from active handlers list
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources for client " + clientName + ": " + e.getMessage());
            }
            synchronized (clientHandlers) {
                clientHandlers.remove(this);
            }
            System.out.println(clientName + " has left the chat.");
            broadcastMessage("SERVER: " + clientName + " has left the chat.");
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     * @param message The message to broadcast.
     */
    private void broadcastMessage(String message) {
        synchronized (clientHandlers) {
            for (ClientHandlerChat handler : clientHandlers) {
                // Send to all clients except the sender (unless it's a server message)
                if (handler != this || message.startsWith("SERVER:")) {
                    handler.sendMessage(message);
                }
            }
        }
    }
}
