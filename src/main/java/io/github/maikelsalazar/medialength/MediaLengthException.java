package io.github.maikelsalazar.medialength;

/**
 * Base exception for failures while parsing or formatting media lengths.
 */
public class MediaLengthException extends IllegalArgumentException {

    /**
     * Creates an exception with the specified detail message.
     *
     * @param message detail message
     */
    public MediaLengthException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause   cause of the media-length operation failure
     */
    public MediaLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
