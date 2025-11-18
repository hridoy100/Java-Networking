# 1.1 Simple Client and Server

This section introduces the fundamental concepts of client-server communication in Java using basic sockets. It demonstrates how a server can listen for incoming connections and how a client can connect to a server to exchange messages.

## Files:

*   `SimpleServer.java`: Implements a basic server that listens on a specific port, accepts a single client connection, receives a message, and sends an echo response.
*   `SimpleClient.java`: Implements a basic client that connects to the server, sends a predefined message, and receives and prints the server's response.

## How to Run:

1.  **Navigate to the directory:**
    ```bash
    cd "1. Step-by-step-learning/1.1_SimpleClientServer"
    ```

2.  **Compile the Java files:**
    ```bash
    javac SimpleServer.java SimpleClient.java
    ```

3.  **Run the Server:**
    Open a new terminal window and execute the server:
    ```bash
    java SimpleServer
    ```
    The server will start and wait for a client connection.

4.  **Run the Client:**
    Open another terminal window and execute the client:
    ```bash
    java SimpleClient
    ```
    The client will connect to the running server, send its message, and display the server's response.

## Expected Output:

**Server Console:**
```
Simple Server started. Listening on port 12345
Waiting for a client to connect...
Client connected: /127.0.0.1
Received from client: Hello from Simple Client!
Sent to client: Server received: Hello from Simple Client!
Server resources closed.
```

**Client Console:**
```
Simple Client started.
Connecting to server at localhost:12345
Connected to server.
Sent to server: Hello from Simple Client!
Received from server: Server received: Hello from Simple Client!
Client resources closed.
```
