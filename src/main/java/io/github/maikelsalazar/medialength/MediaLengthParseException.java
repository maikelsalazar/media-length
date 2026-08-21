package io.github.maikelsalazar.medialength;

/**
 * Thrown when a textual media length cannot be parsed.
 */
public final class MediaLengthParseException extends IllegalArgumentException {

    /**
     * Creates an exception with the specified detail message.
     *
     * @param message detail message
     */
    public MediaLengthParseException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause cause of the parsing failure
     */
    public MediaLengthParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
