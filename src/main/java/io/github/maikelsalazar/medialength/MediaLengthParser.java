package io.github.maikelsalazar.medialength;

import java.time.Duration;

class MediaLengthParser {

    private MediaLengthParser() {
    }

    /**
     * Parses a media length into a {@link Duration}.
     *
     * <p>Accepted formats:
     * <ul>
     *     <li>{@code S+}: seconds, for example {@code "10"}</li>
     *     <li>{@code M+:SS}: minutes and seconds, for example {@code "1:10"}</li>
     * </ul>
     *
     * <p>Variable-length components may contain one or two digits with
     * leading zeroes, but values with three or more digits must not begin
     * with zero. The seconds component in {@code M+:SS} must contain
     * exactly two digits between {@code 00} and {@code 59}.
     *
     * @param lengthToParse string to parse
     * @return the parsed duration, never {@code null}
     * @throws IllegalArgumentException if the input is {@code null},
     *                                  has an invalid format or exceeds
     *                                  the supported range
     */
    public static Duration parse(String lengthToParse) {
        if (lengthToParse == null) {
            throw new IllegalArgumentException("lengthToParse is null");
        }

        String[] components = lengthToParse.split(":", -1);
        return switch (components.length) {
            case 1 -> parseSeconds(components[0]);
            case 2 -> parseMinutesAndSeconds(components[0], components[1]);
            default -> throw new IllegalArgumentException("Invalid length: " + lengthToParse);
        };
    }

    private static Duration parseSeconds(String lengthInSeconds) {
        long seconds = parseComponent(lengthInSeconds, "seconds");

        return Duration.ofSeconds(seconds);
    }

    private static Duration parseMinutesAndSeconds(
            String minutesToParse,
            String secondsToParse
    ) {
        long minutes = parseComponent(minutesToParse, "minutes");
        long seconds = parseExactlyTwoDigitComponentWithMax(secondsToParse, "seconds", 59);

        try {
            return Duration
                    .ofMinutes(minutes)
                    .plusSeconds(seconds);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("minutes and seconds exceed the supported range", e);
        }
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

    private static long parseExactlyTwoDigitComponentWithMax(
            String component,
            String componentName,
            int max
    ) {
        long value = parseComponent(component, componentName);

        if (component.length() != 2) {
            throw new IllegalArgumentException(componentName + " must have exactly two digits");
        }

        if (value > max) {
            throw new IllegalArgumentException(componentName + " must be between 0 and " + max);
        }

        return value;
    }
}
