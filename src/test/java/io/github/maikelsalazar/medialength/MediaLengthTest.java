package io.github.maikelsalazar.medialength;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.Duration;

class MediaLengthTest {

    @Test
    void shouldParseMediaLength() {
        Duration expectedDuration = Duration
                .ofHours(1)
                .plusMinutes(20)
                .plusSeconds(30);

        assertEquals(
                expectedDuration,
                MediaLength.parse("1:20:30")
        );
    }

    @Test
    void shouldFormatMediaLength() {
        Duration duration = Duration
                .ofHours(1)
                .plusMinutes(20)
                .plusSeconds(30);

        assertEquals(
                "1:20:30",
                MediaLength.format(duration)
        );
    }
}
