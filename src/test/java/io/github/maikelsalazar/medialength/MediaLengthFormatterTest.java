package io.github.maikelsalazar.medialength;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

class MediaLengthFormatterTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0",
            "0, 0, 1, 1",
            "0, 0, 59, 59",
            "0, 1, 0, 1:00",
            "0, 59, 1, 59:01",
            "0, 59, 59, 59:59",
            "1, 0, 0, 1:00:00",
            "1, 59, 10, 1:59:10",
            "23, 59, 59, 23:59:59",
            "24, 0, 0, 24:00:00",
            "25, 1, 2, 25:01:02",
            "100, 10, 59, 100:10:59"
    })
    void shouldFormat(
            long hours,
            long minutes,
            long seconds,
            String expectedFormat
    ) {
        Duration durationToFormat = Duration
                .ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);

        assertEquals(expectedFormat, MediaLengthFormatter.format(durationToFormat));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1, 500000000, 1",
            "0, 0, 59, 999999999, 59",
            "0, 59, 1, 999999999, 59:01",
            "0, 59, 59, 999999999, 59:59",
            "1, 59, 10, 500000000, 1:59:10"
    })
    void shouldFormatByTruncatingFractionalSeconds(
            long hours,
            long minutes,
            long seconds,
            long nanos,
            String expectedFormat
    ) {
        Duration durationToFormat = Duration
                .ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusNanos(nanos);

        assertEquals(expectedFormat, MediaLengthFormatter.format(durationToFormat));
    }

    @Test
    void shouldRejectNullDuration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthFormatter.format(null)
        );
    }

    @Test
    void shouldRejectNegativeDuration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthFormatter.format(Duration.ofSeconds(-1))
        );
    }
}
