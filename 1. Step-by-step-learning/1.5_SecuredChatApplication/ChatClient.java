import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.net.ssl.SSLSocketFactory;

/**
 * ChatClient.java
 * This class implements a client for the multi-user chat application with SSL/TLS encryption.
 * It connects to the `ChatServer` securely, allows the user to enter a name, send messages
 * to specific recipients, and receive messages from other participants in the chat.
 *
 * Design Principles:
 * - **Security (SSL/TLS):** Uses `SSLSocket` to encrypt all communication, protecting against
 *   eavesdropping and ensuring message integrity.
 * - **Modularity:** Encapsulates client-side chat logic.
 * - **Resource Management:** Ensures proper closing of sockets and streams.
 * - **User Interaction:** Provides a command-line interface for chat, including sending direct messages.
 * - **Concurrency:** Uses a separate thread to continuously read server messages,
 *   allowing the main thread to handle user input for sending messages.
 * - **Robustness:** Handles server disconnections and I/O errors.
 *
 * Execution Steps:
 * 1. **Generate Truststore:** Before running, you need to import the server's public certificate
 *    into a truststore for the client. First, export the server's certificate from its keystore:
 *    ```bash
 *    keytool -export -alias chatserver -file server.cer -keystore server.keystore
 *    ```
 *    Then, import `server.cer` into a client truststore (e.g., `client.truststore`):
 *    ```bash
 *    keytool -import -alias chatserver -file server.cer -keystore client.truststore
 *    ```
 *    Follow the prompts to set passwords and confirm trust. Remember the truststore password.
 *    Place `client.truststore` and `server.cer` in the same directory as your compiled `.class` files.
 *
 * 2. **Compile:** `javac ChatClient.java`
 *
 * 3. **Run:** `java -Djavax.net.ssl.trustStore=client.truststore -Djavax.net.ssl.trustStorePassword=your_truststore_password ChatClient`
 *    Replace `your_truststore_password` with the password you set during truststore generation.
 *    The client will connect to `localhost` on port 12345 securely.
 *    You will be prompted to enter your name. Then you can start sending messages.
 *    To send a message to a specific user, use the format: `RecipientName: Your message`.
 *    Type 'list' to see online users. Type 'bye' to leave the chat.
 */
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Chat Client started.");
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Scanner scanner = null;

        try {
            // Set system properties for the truststore
            // These should ideally be passed as JVM arguments for production environments
            // System.setProperty("javax.net.ssl.trustStore", "client.truststore");
            // System.setProperty("javax.net.ssl.trustStorePassword", "your_truststore_password");

            System.out.println("Connecting to chat server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
            // Create SSLSocketFactory and SSLSocket
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = sslSocketFactory.createSocket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to chat server securely.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Disconnected from server or error reading: " + e.getMessage());
                }
            });
            readThread.start();

            // Handle initial name registration with the server
            String serverPrompt;
            while ((serverPrompt = in.readLine()) != null) {
                System.out.println(serverPrompt);
                if (serverPrompt.startsWith("SERVER: Enter your unique name:")) {
                    String clientName = scanner.nextLine();
                    out.println(clientName);
                } else if (serverPrompt.startsWith("SERVER: Welcome,")) {
                    break; // Name successfully registered
                } else if (serverPrompt.startsWith("SERVER: Name ") && serverPrompt.contains("is already taken")) {
                    // Loop continues, client will be prompted again
                } else if (serverPrompt.startsWith("SERVER: Name cannot be empty")) {
                    // Loop continues, client will be prompted again
                }
            }

            // Main thread to send messages to the server
            String messageToSend;
            while (true) {
                messageToSend = scanner.nextLine();
                out.println(messageToSend);

                if (messageToSend.equalsIgnoreCase("bye")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
                if (scanner != null) scanner.close();
                System.out.println("Client resources closed. Disconnected from chat.");
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
    }
}
