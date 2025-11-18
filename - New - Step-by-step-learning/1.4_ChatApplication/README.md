# 1.4 Multi-User Chat Application (Direct Messaging)

This section builds upon the previous concepts to create a multi-user chat application that supports direct messaging between clients using their unique names. The server acts as a central hub, routing messages to the intended recipients.

## Files:

*   `ChatServer.java`: The central server component that listens for new client connections. It uses an `ExecutorService` to manage threads for each client and maintains a `ConcurrentHashMap` to map client names to their respective `ClientHandlerChat` instances, enabling efficient routing of direct messages.
*   `ClientHandlerChat.java`: A dedicated handler for each connected client. It manages the client's name registration, reads incoming messages, parses them for recipient names, and forwards them to the appropriate `ClientHandlerChat` instance via the server's map. It also handles client disconnections and provides a list of online users.
*   `ChatClient.java`: The client application that connects to the `ChatServer`. It allows users to enter a unique name, send direct messages to other participants using the format `RecipientName: Your message`, and receive messages in real-time. It uses a separate thread to continuously listen for incoming messages from the server.

## How to Run:

1.  **Navigate to the directory:**
    ```bash
    cd "1. Step-by-step-learning/1.4_ChatApplication"
    ```

2.  **Compile the Java files:**
    ```bash
    javac ChatServer.java ClientHandlerChat.java ChatClient.java
    ```

3.  **Run the Chat Server:**
    Open a new terminal window and execute the server:
    ```bash
    java ChatServer
    ```
    The server will start and listen on port 12345, ready to accept chat clients.

4.  **Run Multiple Chat Clients:**
    Open two or more additional terminal windows. In each, execute the client:
    ```bash
    java ChatClient
    ```
    Each client will connect to the server. You will be prompted to enter a **unique name**. After successful name registration, you can:
    *   Type `list` to see all currently online users.
    *   Send a direct message using the format: `RecipientName: Your message` (e.g., `Alice: Hello Bob!`).
    *   Type `bye` to leave the chat.

## Expected Interaction (Example with two clients, Alice and Bob):

**Server Console:**
```
Chat Server started. Listening on port 12345
Waiting for clients to connect...
New client connected from: /127.0.0.1
Alice has joined the chat from /127.0.0.1
New client connected from: /127.0.0.1
Bob has joined the chat from /127.0.0.1
Alice sent to Bob: Hello Bob!
Bob sent to Alice: Hey Alice!
Alice has left the chat.
```

**Client 1 Console (Alice):**
```
Chat Client started.
Connecting to chat server at localhost:12345
Connected to chat server.
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
Connected to chat server.
SERVER: Enter your unique name:
Bob
SERVER: Welcome, Bob! Type 'list' to see online users. To send a message, use format 'RecipientName: Your message'. Type 'bye' to exit.
Alice (private): Hello Bob!
You: Alice: Hey Alice!
SERVER: Message sent to Alice.
```