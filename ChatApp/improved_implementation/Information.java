package io.github.hridoy100;

public class Information {
    private final String username; // The username of the connected client
    private final NetworkConnection netConnection; // The network connection object for this client

    /**
     * Constructs a new Information object with the specified username and network connection.
     *
     * @param username The username of the client.
     * @param netConnection The {@link NetworkConnection} object associated with this client.
     */
    public Information(String username, NetworkConnection netConnection) {
        this.username = username;
        this.netConnection = netConnection;
    }

    /**
     * Returns the username of the client.
     *
     * @return The client's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the network connection object associated with this client.
     *
     * @return The {@link NetworkConnection} object.
     */
    public NetworkConnection getNetConnection() {
        return netConnection;
    }

    /**
     * Returns a string representation of the Information object.
     *
     * @return A string containing the username and a representation of the network connection.
     */
    @Override
    public String toString() {
        return "Information{" +
               "username='" + username + '\'' +
               ", netConnection=" + netConnection +
               '}';
    }
}