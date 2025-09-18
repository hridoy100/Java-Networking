# 1.4 Multi-User Chat Application

This section brings together all the concepts learned so far (simple client/server, threading, and synchronization) to build a fully functional multi-user chat application. This application demonstrates how to manage multiple concurrent client connections, broadcast messages, and handle client lifecycle events.

## Files:

*   `ChatServer.java`: The central server component that listens for new client connections. It uses an `ExecutorService` to manage threads for each client and maintains a synchronized list of all active `ClientHandlerChat` instances to facilitate message broadcasting.
*   `ClientHandlerChat.java`: A dedicated handler for each connected client. It reads messages from its client, and upon receiving a message, it broadcasts it to all other clients connected to the server. It also handles client joining and leaving messages.
*   `ChatClient.java`: The client application that connects to the `ChatServer`. It allows users to enter a name, send messages to the chat room, and receive messages from other participants in real-time. It uses a separate thread to continuously listen for incoming messages from the server.

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
    Each client will connect to the server. You will be prompted to enter a name. After entering your name, you can start typing messages. Messages sent by one client will be broadcast to all other connected clients. Type `bye` to leave the chat.

## Expected Interaction (Example with two clients, Alice and Bob):

**Server Console:**
```
Chat Server started. Listening on port 12345
Waiting for clients to connect...
New client connected: /127.0.0.1
Alice has joined the chat.
New client connected: /127.0.0.1
Bob has joined the chat.
Alice: Hi everyone!
Bob: Hey Alice!
Alice: bye
Alice has left the chat.
```

**Client 1 Console (Alice):**
```
Chat Client started.
Connecting to chat server at localhost:12345
Connected to chat server.
SERVER: Enter your name:
Alice
SERVER: Alice has joined the chat.
SERVER: Bob has joined the chat.
You: Hi everyone!
Bob: Hey Alice!
You: bye
Client resources closed. Disconnected from chat.
```

**Client 2 Console (Bob):**
```
Chat Client started.
Connecting to chat server at localhost:12345
Connected to chat server.
SERVER: Enter your name:
Bob
SERVER: Alice has joined the chat.
SERVER: Bob has joined the chat.
Alice: Hi everyone!
You: Hey Alice!
SERVER: Alice has left the chat.
```
