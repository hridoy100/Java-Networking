import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MultiThreadedServer.java
 * This class demonstrates a server that can handle multiple client connections concurrently
 * using a thread pool (ExecutorService). Each client connection is delegated to a separate
 * `ClientHandler` thread.
 *
 * Design Principles:
 * - **Concurrency:** Uses `ExecutorService` to manage a pool of threads, improving resource utilization
 *   and preventing the overhead of creating a new thread for every client.
 * - **Modularity:** Separates the concerns of accepting connections (server) and handling individual
 *   client communication (`ClientHandler`).
 * - **Robustness:** Includes basic error handling for server operations.
 * - **Resource Management:** Ensures the server socket and thread pool are properly shut down.
 *
 * Execution Steps:
 * 1. Compile: `javac MultiThreadedServer.java ClientHandler.java`
 * 2. Run: `java MultiThreadedServer`
 *    The server will start and listen on port 12345. It will accept multiple client connections.
 *    You can run multiple `SimpleClient.java` instances to test concurrent connections.
 */
public class MultiThreadedServer {
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 10; // Max concurrent clients

    public static void main(String[] args) {
        System.out.println("Multi-threaded Server started. Listening on port " + PORT);
        ServerSocket serverSocket = null;
        ExecutorService executorService = null;
        int clientCount = 0;

        try {
            // 1. Create a ServerSocket to listen for incoming connections
            serverSocket = new ServerSocket(PORT);

            // 2. Create a fixed-size thread pool to manage client handlers
            executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Waiting for clients to connect...");
            while (true) { // Server runs indefinitely
                // 3. Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Client " + clientCount + " connected: " + clientSocket.getInetAddress().getHostAddress());

                // 4. Create a ClientHandler for the new client and submit it to the thread pool
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientCount);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            // e.printStackTrace(); // Uncomment for detailed stack trace
        } finally {
            // 5. Close resources
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                }
                if (executorService != null) {
                    executorService.shutdown(); // Initiates an orderly shutdown
                    System.out.println("Shutting down thread pool...");
                    // Optional: Wait for all tasks to complete or timeout
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        executorService.shutdownNow(); // Forcefully shut down
                        System.out.println("Thread pool forcefully shut down.");
                    }
                }
                System.out.println("Server resources closed.");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error closing server resources: " + e.getMessage());
            }
        }
    }
}
