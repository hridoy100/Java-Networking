import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SynchronizedServer.java
 * This server demonstrates the use of synchronization to manage access to a shared resource
 * by multiple client handlers. Each client handler will attempt to increment a shared counter.
 * Without proper synchronization, race conditions would occur.
 *
 * Design Principles:
 * - **Concurrency:** Uses `ExecutorService` for managing client threads.
 * - **Modularity:** `ClientHandlerSync` encapsulates client-specific logic, `SharedResource` encapsulates shared state.
 * - **Synchronization:** Explicitly uses `synchronized` methods in `SharedResource` to protect shared data.
 * - **Robustness:** Includes error handling and graceful shutdown of resources.
 *
 * Execution Steps:
 * 1. Compile: `javac SynchronizedServer.java ClientHandlerSync.java SharedResource.java`
 * 2. Run: `java SynchronizedServer`
 *    The server will start and listen on port 12345.
 *    It will accept multiple client connections, and each client will interact with a shared counter.
 */
public class SynchronizedServer {
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 5;
    private static SharedResource sharedCounter = new SharedResource();

    public static void main(String[] args) {
        System.out.println("Synchronized Server started. Listening on port " + PORT);
        ServerSocket serverSocket = null;
        ExecutorService executorService = null;
        int clientCount = 0;

        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Waiting for clients to connect...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Client " + clientCount + " connected: " + clientSocket.getInetAddress().getHostAddress());

                // Pass the shared resource to each client handler
                ClientHandlerSync clientHandler = new ClientHandlerSync(clientSocket, clientCount, sharedCounter);
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
                System.out.println("Server resources closed.");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error closing server resources: " + e.getMessage());
            }
        }
    }
}
