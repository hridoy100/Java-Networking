# 1.3 Synchronization

This section focuses on the critical aspect of synchronization in multi-threaded network applications. When multiple threads access and modify shared resources, race conditions can occur, leading to inconsistent data. This example demonstrates how to use `synchronized` methods to ensure thread safety.

## Files:

*   `SynchronizedServer.java`: A server that utilizes an `ExecutorService` to handle multiple clients. It shares a `SharedResource` instance among all `ClientHandlerSync` threads.
*   `ClientHandlerSync.java`: A client handler that interacts with the `SharedResource`. It processes client commands like `increment` and `get` to modify and read the shared counter.
*   `SharedResource.java`: A class containing a simple counter (`int counter`). Its `incrementAndGet()` and `getCounter()` methods are `synchronized` to ensure that only one thread can access and modify the counter at any given time, thus preventing race conditions.
*   `Client.java`: A client application that connects to the `SynchronizedServer`. It allows users to send `increment`, `get`, and `bye` commands to observe the synchronized behavior of the shared counter.

## How to Run:

1.  **Navigate to the directory:**
    ```bash
    cd "1. Step-by-step-learning/1.3_Synchronization"
    ```

2.  **Compile the Java files:**
    ```bash
    javac SynchronizedServer.java ClientHandlerSync.java SharedResource.java Client.java
    ```

3.  **Run the Server:**
    Open a new terminal window and execute the server:
    ```bash
    java SynchronizedServer
    ```
    The server will start and listen on port 12345.

4.  **Run Multiple Clients:**
    Open one or more additional terminal windows. In each, execute the client:
    ```bash
    java Client
    ```
    Each client will connect to the server. You can type `increment` to increase the shared counter, `get` to see its current value, and `bye` to disconnect. Observe how the counter increments correctly even with multiple clients sending `increment` commands simultaneously, thanks to synchronization.

## Expected Interaction:

**Server Console (example with two clients):**
```
Synchronized Server started. Listening on port 12345
Waiting for clients to connect...
Client 1 connected: /127.0.0.1
ClientHandlerSync 1 created.
Client 2 connected: /127.0.0.1
ClientHandlerSync 2 created.
Client 1 received: increment
Thread-pool-1 incremented counter to: 1
Client 2 received: increment
Thread-pool-2 incremented counter to: 2
Client 1 received: get
Client 2 received: increment
Thread-pool-1 incremented counter to: 3
Client 1 received: bye
Client 1 sent 'bye'. Closing connection.
Client 1 connection closed.
```

**Client 1 Console:**
```
Client started.
Connecting to server at localhost:12345
Connected to server. Type 'increment', 'get', or 'bye'.
Enter command (increment, get, bye): increment
Server: Counter incremented to 1
Enter command (increment, get, bye): get
Server: Current counter value is 2
Enter command (increment, get, bye): bye
Server: Goodbye!
Client resources closed.
```

**Client 2 Console:**
```
Client started.
Connecting to server at localhost:12345
Connected to server. Type 'increment', 'get', or 'bye'.
Enter command (increment, get, bye): increment
Server: Counter incremented to 2
Enter command (increment, get, bye): increment
Server: Counter incremented to 3
Enter command (increment, get, bye): bye
Server: Goodbye!
Client resources closed.
```
