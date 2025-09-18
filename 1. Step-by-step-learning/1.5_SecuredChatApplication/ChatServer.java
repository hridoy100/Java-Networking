import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * ChatServer.java
 * This class implements a multi-user chat server with SSL/TLS encryption.
 * It listens for incoming client connections, and for each connection, it creates a `ClientHandlerChat`
 * to manage communication. It maintains a map of all connected clients (name to handler)
 * to facilitate direct messaging securely.
 *
 * Design Principles:
 * - **Security (SSL/TLS):** Uses `SSLServerSocket` to encrypt all communication, protecting against
 *   eavesdropping and ensuring message integrity.
 * - **Modularity:** Separates server responsibilities (accepting connections) from client handling (`ClientHandlerChat`).
 * - **Concurrency:** Uses an `ExecutorService` to efficiently manage threads for multiple clients.
 * - **Shared State Management:** Uses a `ConcurrentHashMap` to safely manage the map of active client handlers
 *   across multiple threads, allowing for name-based lookup.
 * - **Robustness:** Includes comprehensive error handling and graceful shutdown procedures.
 * - **Scalability:** Designed to handle multiple concurrent clients.
 *
 * Execution Steps:
 * 1. **Generate Keystore:** Before running, you need to generate a keystore for the server.
 *    Use the `keytool` command (part of the JDK) to create a keystore file (e.g., `server.keystore`):
 *    ```bash
 *    keytool -genkeypair -alias chatserver -keyalg RSA -keysize 2048 -keystore server.keystore -validity 3650
 *    ```
 *    Follow the prompts to set passwords and provide server details. Remember the keystore password.
 *    Place `server.keystore` in the same directory as your compiled `.class` files.
 *
 * 2. **Compile:** `javac ChatServer.java ClientHandlerChat.java`
 *
 * 3. **Run:** `java -Djavax.net.ssl.keyStore=server.keystore -Djavax.net.ssl.keyStorePassword=your_keystore_password ChatServer`
 *    Replace `your_keystore_password` with the password you set during keystore generation.
 *    The server will start and listen on port 12345, ready to accept secure client connections.
 */
public class ChatServer {
    private static final int PORT = 12345;
    private static final int THREAD_POOL_SIZE = 20; // Max concurrent clients

    // A thread-safe map to keep track of all connected client handlers, mapping client names to their handlers
    private static ConcurrentHashMap<String, ClientHandlerChat> clientHandlers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started. Listening on port " + PORT);
        SSLServerSocket serverSocket = null;
        ExecutorService executorService = null;

        try {
            // Set system properties for the keystore
            // These should ideally be passed as JVM arguments for production environments
            // System.setProperty("javax.net.ssl.keyStore", "server.keystore");
            // System.setProperty("javax.net.ssl.keyStorePassword", "your_keystore_password");

            // Create SSLServerSocketFactory and SSLServerSocket
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);

            // Optional: Enable specific protocols and cipher suites for stronger security
            // serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
            // serverSocket.setEnabledCipherSuites(new String[]{"TLS_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384"});

            executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Waiting for clients to connect securely...");
            while (true) { // Server runs indefinitely
                Socket clientSocket = serverSocket.accept(); // This will be an SSLSocket
                System.out.println("New client connected securely from: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandlerChat clientHandler = new ClientHandlerChat(clientSocket, clientHandlers);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
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
