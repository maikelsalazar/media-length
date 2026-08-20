package io.github.maikelsalazar.medialength;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MediaLengthParserTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "00, 0",
            "01, 1",
            "09, 9",
            "10, 10",
            "99, 99",
            "100, 100",
            "1000, 1000",
            "9223372036854775807, 9223372036854775807"
    })
    void shouldParseSeconds(String lengthToParse, long expectedSeconds) {
        Duration expected = Duration.ofSeconds(expectedSeconds);

        assertEquals(expected, MediaLengthParser.parse(lengthToParse));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-0",
            "+0",
            "-1",
            "+1",
            " 0",
            "0 ",
            " 0 ",
            "000",
            "001",
            "009",
            "010",
            "0000",
            "0010"
    })
    void shouldRejectInvalidSeconds(String lengthToParse) {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthParser.parse(lengthToParse));
    }

    @Test
    void shouldRejectSecondsOnOverflow() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthParser.parse("9223372036854775808"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc",
            "1.5",
            "1,5",
            "1a",
            ":",
            "1:"
    })
    void shouldRejectInvalidFormat(String lengthToParse) {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthParser.parse(lengthToParse));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "    "})
    void shouldRejectEmptyOrBlankInput(String input) {
        assertThrows(
                IllegalArgumentException.class,
                () -> MediaLengthParser.parse(input)
        );
    }
}
