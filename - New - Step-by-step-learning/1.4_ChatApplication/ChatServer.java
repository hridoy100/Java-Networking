import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ChatServer.java
 * This class implements a multi-user chat server. It listens for incoming client connections,
 * and for each connection, it creates a `ClientHandlerChat` to manage communication.
 * It maintains a map of all connected clients (name to handler) to facilitate direct messaging.
 *
 * Design Principles:
 * - **Modularity:** Separates server responsibilities (accepting connections) from client handling (`ClientHandlerChat`).
 * - **Concurrency:** Uses an `ExecutorService` to efficiently manage threads for multiple clients.
 * - **Shared State Management:** Uses a `ConcurrentHashMap` to safely manage the map of active client handlers
 *   across multiple threads, allowing for name-based lookup.
 * - **Robustness:** Includes comprehensive error handling and graceful shutdown procedures.
 * - **Scalability:** Designed to handle multiple concurrent clients.
 *
 * Execution Steps:
 * 1. Compile: `javac ChatServer.java ClientHandlerChat.java`
 * 2. Run: `java ChatServer`
 *    The server will start and listen on port 12345.
 *    It will accept multiple client connections, and clients can send direct messages to each other.
 */
public class ChatServer {
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 20; // Max concurrent clients

    // A thread-safe map to keep track of all connected client handlers, mapping client names to their handlers
    private static ConcurrentHashMap<String, ClientHandlerChat> clientHandlers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started. Listening on port " + PORT);
        ServerSocket serverSocket = null;
        ExecutorService executorService = null;

        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Waiting for clients to connect...");
            while (true) { // Server runs indefinitely
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandlerChat clientHandler = new ClientHandlerChat(clientSocket, clientHandlers);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                }
                if (executorService != null) {
                    executorService.shutdown();
                    System.out.println("Shutting down thread pool...");
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                        System.out.println("Thread pool forcefully shut down.");
                    }
                }
                System.out.println("Chat Server resources closed.");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error closing server resources: " + e.getMessage());
            }
        }
    }
}
