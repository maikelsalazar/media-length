package io.github.maikelsalazar.medialength;

import java.time.Duration;

class MediaLengthParser {

    private MediaLengthParser() {
    }

    /**
     * Parses a non-negative number of seconds into a {@link Duration}.
     *
     * <p>Accepted format:
     * {@code [0-9]{1,2} | [1-9][0-9]{2,}}.
     * For example, {@code "0"}, {@code "00"}, {@code "09"},
     * {@code "10"} or {@code "1000"}.
     *
     * @param lengthToParse string to parse
     * @return the parsed duration, never {@code null}
     * @throws IllegalArgumentException if the input is {@code null}
     *                                  or has an invalid format
     */
    public static Duration parse(String lengthToParse) {
        if (lengthToParse == null) {
            throw new IllegalArgumentException("lengthToParse is null");
        }

        String[] components = lengthToParse.split(":", -1);
        return switch (components.length) {
            case 1 -> parseSeconds(components[0]);
            default -> throw new IllegalArgumentException("Invalid length: " + lengthToParse);
        };
    }

    private static Duration parseSeconds(String lengthInSeconds) {
        long seconds = parseComponent(lengthInSeconds, "seconds");

        return Duration.ofSeconds(seconds);
    }

    private static long parseComponent(
            String component,
            String componentName
    ) {
        if (component.isBlank()) {
            throw new IllegalArgumentException(componentName + " cannot be empty or blank");
        }

        if (!component.chars().allMatch(c -> c >= '0' && c <= '9')) {
            throw new IllegalArgumentException(componentName + " contains invalid characters");
        }

        if (component.length() > 2 && component.charAt(0) == '0') {
            throw new IllegalArgumentException(componentName + " contains leading zeroes");
        }

        try {
            return Long.parseLong(component);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(componentName + " exceeds the supported range", e);
        }
    }
}
