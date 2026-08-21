package io.github.maikelsalazar.medialength;

/**
 * Thrown when a textual media length cannot be parsed.
 */
public final class MediaLengthParseException extends MediaLengthException {

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

    /**
     * Creates an exception when a component or resulting duration exceeds
     * the supported range.
     *
     * @param valueDescription description of the value that exceeded the range
     * @param cause cause of the parsing failure
     * @return the created parsing exception
     */
    static MediaLengthParseException supportedRangeExceeded(
            String valueDescription,
            Throwable cause
    ) {
        return new MediaLengthParseException(
                valueDescription + " exceeds the supported range",
                cause
        );
    }
}
