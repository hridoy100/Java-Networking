<pre>
_______.___________.    ___      .______      
    /       |           |   /   \     |   _  \     
   |   (----`---|  |----`  /  ^  \    |  |_)  |    
    \   \       |  |      /  /_\  \   |      /     
.----)   |      |  |     /  _____  \  |  |\  \----.
|_______/       |__|    /__/     \__\ | _| `._____|
                                                   
.___________. __    __   __       _______.
|           ||  |  |  | |  |     /       |
`---|  |----`|  |__|  | |  |    |   (----`
    |  |     |   __   | |  |     \   \    
    |  |     |  |  |  | |  | .----)   |   
    |__|     |__|  |__| |__| |_______/    
                                          
.______       _______ .______     ______        _______. __  .___________.
|   _  \     |   ____||   _  \   /  __  \      /       ||  | |           |
|  |_)  |    |  |__   |  |_)  | |  |  |  |    |   (----`|  | `---|  |----`
|      /     |   __|  |   ___/  |  |  |  |     \   \    |  |     |  |     
|  |\  \----.|  |____ |  |      |  `--'  | .----)   |   |  |     |  |     
| _| `._____||_______|| _|       \______/  |_______/    |__|     |__|     
                                                                          
  ______   .______     ____    ____ 
 /  __  \  |   _  \    \   \  /   / 
|  |  |  | |  |_)  |    \   \/   /  
|  |  |  | |      /      \_    _/   
|  `--'  | |  |\  \----.   |  |     
 \______/  | _| `._____|   |__|
</pre>

# Java-Networking

This repository contains a collection of simple Java networking projects.

## Project Folders

Here's a list of the main project folders in this repository:

*   [ChatApp](https://github.com/hridoy100/Java-Networking/tree/master/ChatApp)
    *   [Improved Implementation](https://github.com/hridoy100/Java-Networking/tree/master/ChatApp/improved_implementation) - A multi-client chat application with improved code and comments.
*   [docs](https://github.com/hridoy100/Java-Networking/tree/master/docs) - Documentation and GitHub Pages website for the project.
*   [reader_writer_thread](https://github.com/hridoy100/Java-Networking/tree/master/reader_writer_thread)
    *   [Improved Implementation](https://github.com/hridoy100/Java-Networking/tree/master/reader_writer_thread/improved_implementation) - Demonstrates client-server communication using separate reader and writer threads with improved code and comments.
*   [simple_client_server_using_threading](https://github.com/hridoy100/Java-Networking/tree/master/simple_client_server_using_threading)
    *   [Improved Implementation](https://github.com/hridoy100/Java-Networking/tree/master/simple_client_server_using_threading/improved_implementation) - A simple client-server application with multithreading with improved code and comments.
*   [simpleclient_server_without_thread](https://github.com/hridoy100/Java-Networking/tree/master/simpleclient_server_without_thread)
    *   [Improved Implementation](https://github.com/hridoy100/Java-Networking/tree/master/simpleclient_server_without_thread/improved_implementation) - A basic client-server application without multithreading with improved code and comments.
*   [Threading](https://github.com/hridoy100/Java-Networking/tree/master/Threading)
    *   [Improved Implementation](https://github.com/hridoy100/Java-Networking/tree/master/Threading/improved_implementation) - Examples demonstrating various Java threading concepts with improved code and comments.

## Projects

### Simple Client-Server without Thread

This project demonstrates a basic client-server application where a client can send a message to the server, and the server will respond with the same message in uppercase. This project does not use threading, so it can only handle one client at a time.

#### How to Run

1.  **Compile the code:**
    Open a terminal and navigate to the `simpleclient_server_without_thread/src` directory. Then, compile the Java files using the following command:
    ```bash
    javac io/github/hridoy100/Server.java io/github/hridoy100/Client.java
    ```
2.  **Run the server:**
    In the same terminal, run the server using the following command:
    ```bash
    java io.github.hridoy100.Server
    ```
3.  **Run the client:**
    Open a new terminal and navigate to the `simpleclient_server_without_thread/src` directory. Then, run the client using the following command:
    ```bash
    java io.github.hridoy100.Client
    ```
4.  **Interact with the application:**
    Enter a message in the client terminal and press Enter. The server will respond with the uppercase version of the message, which will be displayed in the client terminal.

### Simple Client-Server with Threading

This project is an extension of the previous one, with the addition of multithreading. This allows the server to handle multiple clients simultaneously. Each client connection is handled in a separate thread, so the server can remain responsive to new clients while processing existing ones.

#### How to Run

1.  **Compile the code:**
    Open a terminal and navigate to the `simple_client_server_using_threading/src` directory. Then, compile the Java files using the following command:
    ```bash
    javac io/github/hridoy100/Server.java io/github/hridoy100/Client.java
    ```
2.  **Run the server:**
    In the same terminal, run the server using the following command:
    ```bash
    java io.github.hridoy100.Server
    ```
3.  **Run the client:**
    Open a new terminal and navigate to the `simple_client_server_using_threading/src` directory. Then, run the client using the following command:
    ```bash
    java io.github.hridoy100.Client
    ```
4.  **Interact with the application:**
    You can now run multiple clients and they will all be able to connect to the server and send messages.

### Reader-Writer Thread

This project demonstrates a more advanced client-server application where both the client and the server use separate threads for reading and writing. This allows for full-duplex communication, where the client and server can send and receive messages simultaneously.

#### How to Run

1.  **Compile the code:**
    Open a terminal and navigate to the `reader_writer_thread/src` directory. Then, compile the Java files using the following command:
    ```bash
    javac io/github/hridoy100/Server.java io/github/hridoy100/Client.java io/github/hridoy100/ReaderThread.java io/github/hridoy100/WriterThread.java
    ```
2.  **Run the server:**
    In the same terminal, run the server using the following command:
    ```bash
    java io.github.hridoy100.Server
    ```
3.  **Run the client:**
    Open a new terminal and navigate to the `reader_writer_thread/src` directory. Then, run the client using the following command:
    ```bash
    java io.github.hridoy100.Client
    ```
4.  **Interact with the application:**
    The client and server can now send and receive messages simultaneously.

### Threading

This project contains a collection of examples demonstrating different concepts in Java threading.

*   **`MainThread.java`**: Demonstrates basic thread operations like getting the current thread and setting its name.
*   **`RunnableThread.java`**: Shows how to create and run a new thread by implementing the `Runnable` interface.
*   **`Synchronization.java`**: Illustrates how to use the `synchronized` keyword to prevent race conditions and ensure thread safety.
*   **`PCBlockingQueue.java`**, **`Producer.java`**, and **`Consumer.java`**: Implement a producer-consumer pattern using a `BlockingQueue` to safely exchange data between threads.

#### How to Run

Each of the Java files in this project can be run individually. For example, to run the `Synchronization` example, navigate to the `Threading/src` directory and use the following commands:

```bash
javac io/github/hridoy100/Synchronization.java
java io.github.hridoy100.Synchronization
```

### ChatApp

This project is a multi-client chat application that allows users to connect to a server, set a username, and send messages to other users. The server maintains a list of connected clients and forwards messages between them. It also supports commands like `list` to see connected users and `ip` to get your IP address.

#### How to Run

1.  **Compile the code:**
    Open a terminal and navigate to the `ChatApp/src` directory. Then, compile the Java files using the following command:
    ```bash
    javac io/github/hridoy100/*.java
    ```
2.  **Run the server:**
    In the same terminal, run the server using the following command:
    ```bash
    java io.github.hridoy100.ServerMain
    ```
3.  **Run the client:**
    Open a new terminal and navigate to the `ChatApp/src` directory. Then, run the client using the following command:
    ```bash
    java io.github.hridoy100.ClientMain
    ```
4.  **Interact with the application:**
    Enter a username and start sending messages to other users. You can use the `list` command to see the list of connected users.

## Contributing

Contributions are welcome! If you have any suggestions, improvements, or bug reports, please open an issue on GitHub.

## Further Improvements

Here are some suggestions for further improvements to this repository:

*   **Add a build system:** The current method of compiling and running the projects using `javac` and `java` is cumbersome. Consider using a build system like [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/) to automate the build process.
*   **Add unit tests:** There are no unit tests in the repository. Adding unit tests would help to ensure the correctness of the code and prevent regressions. Consider using a testing framework like [JUnit](https://junit.org/junit5/) to write and run the tests.
*   **Refactor the code:** The code in some of the projects could be refactored for clarity and efficiency. For example, the `ChatApp` project could be refactored to use a more object-oriented design.
*   **Add comments:** The code is not well-commented. Adding comments would make it easier to understand the code and its functionality.
*   **Use a logging framework:** The current projects use `System.out.println` for logging. Consider using a logging framework like [Log4j](https://logging.apache.org/log4j/2.x/) or [SLF4J](http://www.slf4j.org/) to provide more flexible and configurable logging.

## Acknowledgements

A special thanks to **Professor Rifat Shahriyar**, my course teacher, ([https://rifatshahriyar.github.io/](https://rifatshahriyar.github.io/)) for his invaluable teaching and guidance to this project.