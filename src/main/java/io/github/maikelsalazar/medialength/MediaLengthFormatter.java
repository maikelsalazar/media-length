package io.github.maikelsalazar.medialength;

import java.time.Duration;

/**
 * Formats non-negative durations as media-length strings.
 *
 * <p>Fractional seconds are truncated.
 */
final class MediaLengthFormatter {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR =
            MINUTES_PER_HOUR * SECONDS_PER_MINUTE;

    private MediaLengthFormatter() {
    }

    /**
     * Formats the given duration using the shortest applicable media-length
     * format.
     *
     * @param duration duration to format
     * @return the formatted media length
     * @throws MediaLengthFormatException if {@code duration} is
     *                                  {@code null} or negative
     */
    static String format(Duration duration) {
        if (duration == null) {
            throw new MediaLengthFormatException("duration is null");
        }

        if (duration.isNegative()) {
            throw new MediaLengthFormatException(
                    "duration cannot be negative"
            );
        }

        long totalSeconds = duration.toSeconds();

        if (totalSeconds >= SECONDS_PER_HOUR) {
            return String.format(
                    "%d:%s:%s",
                    duration.toHours(),
                    exactlyTwoDigits(duration.toMinutesPart()),
                    exactlyTwoDigits(duration.toSecondsPart())
            );
        }

        if (totalSeconds >= SECONDS_PER_MINUTE) {
            return String.format(
                    "%d:%s",
                    duration.toMinutesPart(),
                    exactlyTwoDigits(duration.toSecondsPart())
            );
        }

        return Long.toString(totalSeconds);
    }

    private static String exactlyTwoDigits(int number) {
        return number < 10
                ? "0" + number
                : Integer.toString(number);
    }
}
