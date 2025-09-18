import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClientHandlerChat.java
 * This class handles communication with a single chat client. It reads messages from its client
 * and forwards them to a specific recipient based on the recipient's name.
 *
 * Design Principles:
 * - **Modularity:** Encapsulates client-specific chat logic.
 * - **Concurrency:** Implements `Runnable` to allow each client to be handled in a separate thread.
 * - **Resource Management:** Ensures proper closing of client-specific sockets and streams.
 * - **Direct Messaging:** Facilitates one-to-one communication between clients via the server.
 * - **Robustness:** Handles client disconnections and invalid recipient names gracefully.
 */
public class ClientHandlerChat implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private ConcurrentHashMap<String, ClientHandlerChat> clientHandlers; // Reference to all connected clients

    /**
     * Constructor for ClientHandlerChat.
     * @param socket The client socket connected to this handler.
     * @param clientHandlers A map of all active client handlers in the chat server (name -> handler).
     */
    public ClientHandlerChat(Socket socket, ConcurrentHashMap<String, ClientHandlerChat> clientHandlers) {
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

            // 1. Ask client for their name and register it
            while (true) {
                out.println("SERVER: Enter your unique name:");
                clientName = in.readLine();
                if (clientName == null) {
                    // Client disconnected before providing a name
                    return;
                }
                clientName = clientName.trim();
                if (clientName.isEmpty()) {
                    out.println("SERVER: Name cannot be empty. Please try again.");
                } else if (clientHandlers.containsKey(clientName)) {
                    out.println("SERVER: Name '" + clientName + "' is already taken. Please choose another.");
                } else {
                    clientHandlers.put(clientName, this);
                    System.out.println(clientName + " has joined the chat from " + clientSocket.getInetAddress().getHostAddress());
                    sendMessage("SERVER: Welcome, " + clientName + "! Type 'list' to see online users. To send a message, use format 'RecipientName: Your message'. Type 'bye' to exit.");
                    break;
                }
            }

            String clientMessage;
            // 2. Read messages from the client and process them
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("bye")) {
                    break;
                } else if (clientMessage.equalsIgnoreCase("list")) {
                    listOnlineUsers();
                } else if (clientMessage.contains(":")) {
                    int colonIndex = clientMessage.indexOf(":");
                    String recipientName = clientMessage.substring(0, colonIndex).trim();
                    String messageContent = clientMessage.substring(colonIndex + 1).trim();

                    if (recipientName.isEmpty() || messageContent.isEmpty()) {
                        sendMessage("SERVER: Invalid message format. Use 'RecipientName: Your message'.");
                        continue;
                    }

                    ClientHandlerChat recipientHandler = clientHandlers.get(recipientName);
                    if (recipientHandler != null) {
                        recipientHandler.sendMessage(clientName + " (private): " + messageContent);
                        sendMessage("SERVER: Message sent to " + recipientName + ".");
                        System.out.println(clientName + " sent to " + recipientName + ": " + messageContent);
                    } else {
                        sendMessage("SERVER: User '" + recipientName + "' not found or offline.");
                    }
                } else {
                    sendMessage("SERVER: Unknown command or invalid message format. Type 'list' or 'RecipientName: Your message'.");
                }
            }

        } catch (IOException e) {
            System.err.println("Error handling client " + clientName + ": " + e.getMessage());
        } finally {
            // 3. Clean up resources and remove from active handlers map
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources for client " + clientName + ": " + e.getMessage());
            }
            if (clientName != null) {
                clientHandlers.remove(clientName);
                System.out.println(clientName + " has left the chat.");
            }
        }
    }

    /**
     * Sends a list of currently online users to this client.
     */
    private void listOnlineUsers() {
        StringBuilder userList = new StringBuilder("SERVER: Online users: ");
        boolean first = true;
        for (String name : clientHandlers.keySet()) {
            if (!first) {
                userList.append(", ");
            }
            userList.append(name);
            first = false;
        }
        sendMessage(userList.toString());
    }
}
