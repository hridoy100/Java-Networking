package io.github.hridoy100;

import java.io.Serializable;

/**
 * A simple data transfer object (DTO) to encapsulate message content
 * for communication between client and server.
 * This class is {@link Serializable} to allow its instances to be
 * transmitted over network streams.
 */
public class Data implements Serializable, Cloneable {

    // A unique identifier for serialization. Recommended for Serializable classes.
    private static final long serialVersionUID = 1L;

    private String message; // The message content carried by this Data object

    /**
     * Default constructor.
     */
    public Data() {
        // Default constructor
    }

    /**
     * Constructs a Data object with a specified message.
     *
     * @param message The message string to be encapsulated.
     */
    public Data(String message) {
        this.message = message;
    }

    /**
     * Returns the message content of this Data object.
     *
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of this Data object.
     *
     * @param message The new message string to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Creates and returns a copy of this object. The precise meaning of "copy" may depend on the class of the object.
     * The general intent is that, for any object x, the expression x.clone() != x will be true,
     * and that the expression x.clone().getClass() == x.getClass() will be true,
     * but these are not absolute requirements.
     *
     * @return A clone of this instance.
     * @throws CloneNotSupportedException If the object's class does not support the {@code Cloneable} interface.
     *                                    Subclasses that override the {@code clone} method can also throw this
     *                                    exception to indicate that an instance cannot be cloned.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns a string representation of the Data object, which is the encapsulated message.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "Data{message='" + message + "'}";
    }
}