package io.github.hridoy100;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class manages a single network connection, providing methods to
 * send and receive objects over a {@link Socket}. It handles the creation
 * of {@link ObjectOutputStream} and {@link ObjectInputStream} and ensures
 * proper resource closure.
 */
public class NetworkConnection implements Closeable {
    private final Socket socket; // The underlying network socket for this connection
    private final ObjectInputStream ois;   // Stream to read objects from the socket
    private final ObjectOutputStream oos; // Stream to write objects to the socket

    /**
     * Constructs a NetworkConnection using an already established {@link Socket}.
     * Initializes the object input and output streams.
     *
     * @param sock The connected {@link Socket} to be managed by this instance.
     * @throws IOException If an I/O error occurs when creating the streams.
     */
    public NetworkConnection(Socket sock) throws IOException {
        this.socket = sock;
        // ObjectOutputStream must be created before ObjectInputStream on both client and server
        // to avoid potential deadlocks or stream corruption.
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Constructs a NetworkConnection by establishing a new {@link Socket} connection
     * to the specified IP address and port.
     *
     * @param ip The IP address of the remote host to connect to.
     * @param port The port number of the remote host.
     * @throws IOException If an I/O error occurs when creating the socket or streams.
     */
    public NetworkConnection(String ip, int port) throws IOException {
        this(new Socket(ip, port)); // Call the other constructor with a newly created socket
    }

    /**
     * Writes an object to the connected socket.
     *
     * @param obj The object to be written. Must be {@link java.io.Serializable}.
     * @throws IOException If an I/O error occurs during writing.
     */
    public void write(Object obj) throws IOException {
        oos.writeObject(obj);
        oos.flush(); // Ensure the object is sent immediately
    }

    /**
     * Reads an object from the connected socket.
     *
     * @return The object read from the socket.
     * @throws IOException If an I/O error occurs during reading.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public Object read() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    /**
     * Returns the underlying {@link Socket} associated with this network connection.
     *
     * @return The {@link Socket} instance.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Closes the underlying socket and its associated streams, releasing network resources.
     * This method should be called when the connection is no longer needed.
     *
     * @throws IOException If an I/O error occurs during closing.
     */
    @Override
    public void close() throws IOException {
        try {
            if (ois != null) ois.close();
        } finally {
            try {
                if (oos != null) oos.close();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }
    }
}
