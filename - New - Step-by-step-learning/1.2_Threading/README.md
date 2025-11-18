# 1.2 Threading

This section demonstrates how to build a server that can handle multiple client connections concurrently using Java's threading capabilities and `ExecutorService` (thread pools). This is a crucial step towards building scalable network applications.

## Files:

*   `MultiThreadedServer.java`: An enhanced server that uses an `ExecutorService` to manage a pool of threads. For each incoming client connection, it submits a `ClientHandler` task to the thread pool, allowing it to serve multiple clients simultaneously.
*   `ClientHandler.java`: A `Runnable` class responsible for handling the communication with a single client. Each instance runs in its own thread, reading messages from its client and sending responses.
*   `SimpleClient.java`: A client application (similar to the one in 1.1) that connects to the server, sends messages, and receives responses. This version allows for interactive input and can be run multiple times to simulate multiple clients.

## How to Run:

1.  **Navigate to the directory:**
    ```bash
    cd "1. Step-by-step-learning/1.2_Threading"
    ```

2.  **Compile the Java files:**
    ```bash
    javac MultiThreadedServer.java ClientHandler.java SimpleClient.java
    ```

3.  **Run the Server:**
    Open a new terminal window and execute the server:
    ```bash
    java MultiThreadedServer
    ```
    The server will start and listen on port 12345, ready to accept multiple client connections.

4.  **Run Multiple Clients:**
    Open one or more additional terminal windows. In each, execute the client:
    ```bash
    java SimpleClient
    ```
    Each client will connect to the server. You can type messages in each client's console, and the server will process them concurrently. Type `bye` in a client to disconnect it.

## Expected Interaction:

**Server Console:**
```
Multi-threaded Server started. Listening on port 12345
Waiting for clients to connect...
Client 1 connected: /127.0.0.1
ClientHandler 1 created for /127.0.0.1
Client 2 connected: /127.0.0.1
ClientHandler 2 created for /127.0.0.1
Client 1 received: Hello from Client 1
Client 2 received: Hello from Client 2
Client 1 received: bye
Client 1 sent 'bye'. Closing connection.
Client 1 connection closed.
```

**Client 1 Console:**
```
Simple Client started.
Connecting to server at localhost:12345
Connected to server. Type 'bye' to exit.
You: Hello from Client 1
Server: Server received from Client 1: Hello from Client 1
You: bye
Client resources closed.
```

**Client 2 Console:**
```
Simple Client started.
Connecting to server at localhost:12345
Connected to server. Type 'bye' to exit.
You: Hello from Client 2
Server: Server received from Client 2: Hello from Client 2
...
```
