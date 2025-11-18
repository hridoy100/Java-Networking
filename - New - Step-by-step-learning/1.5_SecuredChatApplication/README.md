# 1.5 Multi-User Chat Application (Secured with SSL/TLS)

This section enhances the multi-user chat application by implementing SSL/TLS encryption to secure communication between clients and the server. This addresses critical vulnerabilities such as eavesdropping, message tampering, and impersonation, which are prevalent in unencrypted network communication.

## Security Enhancements:

*   **Encryption:** All data exchanged between clients and the server is encrypted using SSL/TLS, preventing unauthorized parties from reading the chat content.
*   **Authentication:** The server presents a digital certificate to clients, allowing clients to verify the server's identity and preventing Man-in-the-Middle (MitM) attacks where an attacker might impersonate the server.
*   **Integrity:** SSL/TLS ensures message integrity, meaning any tampering with messages during transit will be detected.

## Files:

*   `ChatServer.java`: The secure server component. It uses `SSLServerSocket` to establish encrypted connections. It requires a keystore containing its private key and certificate to authenticate itself to clients.
*   `ClientHandlerChat.java`: (Unchanged from 1.4) This class continues to handle individual client communication, but now operates over secure `SSLSocket` streams provided by the server.
*   `ChatClient.java`: The secure client application. It uses `SSLSocket` to establish encrypted connections with the server. It requires a truststore containing the server's public certificate to verify the server's identity.

## How to Run:

### Prerequisites:

Before running the secure chat application, you need to generate a keystore for the server and a truststore for the client using Java's `keytool` utility.

1.  **Generate Server Keystore (`server.keystore`):
    **This file will contain the server's private key and public certificate.**
    ```bash
    keytool -genkeypair -alias chatserver -keyalg RSA -keysize 2048 -keystore server.keystore -validity 3650
    ```
    *   **`keytool -genkeypair`**: Generates a public/private key pair.
    *   **`-alias chatserver`**: An alias for the entry in the keystore.
    *   **`-keyalg RSA`**: Specifies the RSA algorithm for key generation.
    *   **`-keysize 2048`**: Specifies a 2048-bit key size.
    *   **`-keystore server.keystore`**: The name of the keystore file to create.
    *   **`-validity 3650`**: The certificate will be valid for 3650 days (10 years).

    Follow the prompts:
    *   **`Enter keystore password:`**: Choose a strong password (e.g., `serverpass`).
    *   **`What is your first and last name?`**: Enter `localhost` (or the server's hostname/IP if not running locally).
    *   Fill in other details as prompted. Remember the keystore password.
    *   **`Enter key password for <chatserver>`**: You can press Enter to use the same password as the keystore.

2.  **Export Server Certificate (`server.cer`):
    **This extracts the server's public certificate, which clients will use to trust the server.**
    ```bash
    keytool -export -alias chatserver -file server.cer -keystore server.keystore
    ```
    *   **`-export`**: Exports the certificate.
    *   **`-alias chatserver`**: The alias of the certificate to export.
    *   **`-file server.cer`**: The name of the file to export the certificate to.
    *   **`-keystore server.keystore`**: The keystore from which to export.

    You will be prompted for the keystore password.

3.  **Generate Client Truststore (`client.truststore`):
    **This file will contain the server's public certificate, allowing the client to trust the server.**
    ```bash
    keytool -import -alias chatserver -file server.cer -keystore client.truststore
    ```
    *   **`-import`**: Imports a certificate.
    *   **`-alias chatserver`**: An alias for the imported certificate in the truststore.
    *   **`-file server.cer`**: The certificate file to import.
    *   **`-keystore client.truststore`**: The name of the truststore file to create.

    Follow the prompts:
    *   **`Enter keystore password:`**: Choose a strong password (e.g., `clientpass`).
    *   **`Trust this certificate? [no]:`**: Type `yes` and press Enter.

    **Place `server.keystore`, `server.cer`, and `client.truststore` files in the `1.5_SecuredChatApplication` directory.**

### Running the Application:

1.  **Navigate to the directory:**
    ```bash
    cd "1. Step-by-step-learning/1.5_SecuredChatApplication"
    ```

2.  **Compile the Java files:**
    ```bash
    javac ChatServer.java ClientHandlerChat.java ChatClient.java
    ```

3.  **Run the Secure Chat Server:**
    Open a new terminal window and execute the server, providing the keystore details:
    ```bash
    java -Djavax.net.ssl.keyStore=server.keystore -Djavax.net.ssl.keyStorePassword=serverpass ChatServer
    ```
    Replace `serverpass` with your actual keystore password.
    The server will start and listen on port 12345, ready to accept secure client connections.

4.  **Run Multiple Secure Chat Clients:**
    Open two or more additional terminal windows. In each, execute the client, providing the truststore details:
    ```bash
    java -Djavax.net.ssl.trustStore=client.truststore -Djavax.net.ssl.trustStorePassword=clientpass ChatClient
    ```
    Replace `clientpass` with your actual truststore password.
    Each client will connect securely to the server. You will be prompted to enter a **unique name**. After successful name registration, you can:
    *   Type `list` to see all currently online users.
    *   Send a direct message using the format: `RecipientName: Your message` (e.g., `Alice: Hello Bob!`).
    *   Type `bye` to leave the chat.

## Expected Interaction (Example with two clients, Alice and Bob):

**Server Console:**
```
Chat Server started. Listening on port 12345
Waiting for clients to connect securely...
New client connected securely from: /127.0.0.1
Alice has joined the chat from /127.0.0.1
New client connected securely from: /127.0.0.1
Bob has joined the chat from /127.0.0.1
Alice sent to Bob: Hello Bob!
Bob sent to Alice: Hey Alice!
Alice has left the chat.
```

**Client 1 Console (Alice):**
```
Chat Client started.
Connecting to chat server at localhost:12345
Connected to chat server securely.
SERVER: Enter your unique name:
Alice
SERVER: Welcome, Alice! Type 'list' to see online users. To send a message, use format 'RecipientName: Your message'. Type 'bye' to exit.
You: list
SERVER: Online users: Alice, Bob
You: Bob: Hello Bob!
SERVER: Message sent to Bob.
Bob (private): Hey Alice!
You: bye
Client resources closed. Disconnected from chat.
```

**Client 2 Console (Bob):**
```
Chat Client started.
Connecting to chat server at localhost:12345
Connected to chat server securely.
SERVER: Enter your unique name:
Bob
SERVER: Welcome, Bob! Type 'list' to see online users. To send a message, use format 'RecipientName: Your message'. Type 'bye' to exit.
Alice (private): Hello Bob!
You: Alice: Hey Alice!
SERVER: Message sent to Alice.
```
