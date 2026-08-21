package io.github.maikelsalazar.medialength;

import java.time.Duration;

/**
 * Provides operations for parsing and formatting media lengths.
 *
 * <p>A media length represents a non-negative duration using one of
 * the following formats:
 * <ul>
 *     <li>{@code S+}: seconds, for example {@code "10"}</li>
 *     <li>{@code M+:SS}: minutes and seconds, for example {@code "1:10"}</li>
 *     <li>{@code H+:MM:SS}: hours, minutes and seconds,
 *         for example {@code "1:20:30"}</li>
 * </ul>
 *
 * <p>Variable-length components may contain one or two digits with
 * leading zeroes, but values with three or more digits must not begin
 * with zero. Fixed-width minute and second components must contain
 * exactly two digits between {@code 00} and {@code 59}.
 */
public final class MediaLength {

    private MediaLength() {
    }

    /**
     * Parses a media-length string into a {@link Duration}.
     *
     * @param mediaLength media length to parse
     * @return the parsed duration, never {@code null}
     * @throws MediaLengthParseException if {@code mediaLength} is
     *                                  {@code null}, has an invalid format
     *                                  or exceeds the supported range
     */
    public static Duration parse(String mediaLength) {
        return MediaLengthParser.parse(mediaLength);
    }

    /**
     * Formats a duration as a media-length string.
     *
     * <p>The shortest applicable format is selected:
     * <ul>
     *     <li>{@code S+} for durations shorter than one minute</li>
     *     <li>{@code M+:SS} for durations shorter than one hour</li>
     *     <li>{@code H+:MM:SS} for durations of one hour or longer</li>
     * </ul>
     *
     * <p>Fractional seconds are truncated rather than rounded.
     *
     * @param duration duration to format
     * @return the formatted media length, never {@code null}
     * @throws IllegalArgumentException if {@code duration} is
     *                                  {@code null} or negative
     */
    public static String format(Duration duration) {
        return MediaLengthFormatter.format(duration);
    }
}
