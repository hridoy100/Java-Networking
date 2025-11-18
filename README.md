# Java Networking Playground

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This repository contains a collection of Java networking projects, ranging from simple client-server applications to more complex examples involving multithreading, synchronization, and secure communication. It serves as a practical guide for learning and implementing networking concepts in Java.

![Star this Repo](star.png)


## 📂 Project Structure

The repository is organized into several directories, each demonstrating a specific concept or a complete application.

-   **`-New-Step-by-step-learning`**: A guided progression through core networking topics.
    -   `1.1_SimpleClientServer`: Basic client-server communication.
    -   `1.2_Threading`: Handling multiple clients with a multi-threaded server.
    -   `1.3_Synchronization`: Demonstrating thread synchronization with shared resources.
    -   `1.4_ChatApplication`: A complete multi-user chat application.
    -   `1.5_SecuredChatApplication`: A chat application secured with SSL/TLS.
-   **`ChatApp`**: A more refined multi-client chat application.
-   **`reader_writer_thread`**: Demonstrates full-duplex (simultaneous read/write) communication using separate threads.
-   **`simple_client_server_using_threading`**: A basic client-server model that uses multithreading.
-   **`simpleclient_server_without_thread`**: A fundamental client-server application that can only handle a single client at a time.
-   **`Threading`**: Examples of core Java threading concepts, including the Producer-Consumer problem.
-   **`Thread-Pooling`**: Beginner to expert examples of using thread pools.

## 🚀 Getting Started

The projects in this repository are built using standard Java and can be compiled and run from the command line. For detailed instructions, please refer to the `README.md` file within each specific project directory.

### General Instructions

1.  **Navigate to a project's source directory.**
    For example, to run the simple client-server project:
    ```bash
    cd "-New-Step-by-step-learning/1.1_SimpleClientServer"
    ```

2.  **Compile the Java files.**
    ```bash
    javac *.java
    ```

3.  **Run the Server.**
    Open a terminal and run the main server class.
    ```bash
    java SimpleServer
    ```

4.  **Run the Client.**
    Open a *new* terminal and run the client class.
    ```bash
    java SimpleClient
    ```

## 🤝 Contributing

**Contributions are highly encouraged!** If you have any suggestions, improvements, or bug reports, please feel free to open an issue or submit a pull request on GitHub. Your input helps make this project better!

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 🙏 Acknowledgements

Acknowledging my BUET CSE course teacher, [Professor Rifat Shahriyar](https://rifatshahriyar.github.io) for his invaluable teaching and guidance.

## 📄 License

This project is distributed under the MIT License. See the `LICENSE` file for more information.