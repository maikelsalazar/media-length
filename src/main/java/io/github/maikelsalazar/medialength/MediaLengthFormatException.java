package io.github.maikelsalazar.medialength;

/**
 * Thrown when a duration cannot be formatted as a media length.
 */
public final class MediaLengthFormatException extends MediaLengthException {

    /**
     * Creates an exception with the specified detail message.
     *
     * @param message detail message
     */
    public MediaLengthFormatException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause cause of the formatting failure
     */
    public MediaLengthFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
