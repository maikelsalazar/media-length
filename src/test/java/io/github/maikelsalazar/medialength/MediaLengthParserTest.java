package io.github.maikelsalazar.medialength;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

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
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse));
    }

    @Test
    void shouldRejectSecondsOnOverflow() {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse("9223372036854775808"));
    }

    @ParameterizedTest
    @CsvSource({
            "0:00, 0, 0",
            "1:10, 1, 10",
            "00:00, 0, 0",
            "02:30, 2, 30",
            "59:59, 59, 59",
            "90:59, 90, 59",
            "100:10, 100, 10",
            "153722867280912930:00, 153722867280912930, 0",
            "153722867280912930:07, 153722867280912930, 7",
            "153722867280912929:59, 153722867280912929, 59"
    })
    void shouldParseMinutesAndSeconds(
            String lengthToParse,
            long expectedMinutes,
            long expectedSeconds
    ) {
        Duration expected = Duration
                .ofMinutes(expectedMinutes)
                .plusSeconds(expectedSeconds);

        assertEquals(expected, MediaLengthParser.parse(lengthToParse));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "000:00",
            "009:00",
            "010:00",
            "0000:00",
            "0010:00",

            "-0:00",
            "+0:10",
            "0:-05",
            "0:+10",

            " 0:00",
            "0:00 ",
            " 00:00 ",

            "abc:00",
            "1.5:00",
            "1,5:00",
            "1a:00",

            ":",
            ":00",
            "1:",
            "1::",

            "1:0",
            "1:000",
            "1:001",

            "0:60",
            "0:99",
            "10:60"
    })
    void shouldRejectInvalidMinutesAndSeconds(String lengthToParse) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "153722867280912930:08",
            "153722867280912930:59",
            "153722867280912931:00"
    })
    void shouldRejectMinutesAndSecondsOnOverflow(String lengthToParse) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0:00:00, 0, 0, 0",
            "0:01:10, 0, 1, 10",
            "0:59:59, 0, 59, 59",

            "00:00:00, 0, 0, 0",
            "00:02:30, 0, 2, 30",
            "99:59:59, 99, 59, 59",

            "100:10:59, 100, 10, 59",

            "2562047788015215:30:07, 2562047788015215, 30, 7"
    })
    void shouldParseHoursAndMinutesAndSeconds(
            String lengthToParse,
            long expectedHours,
            long expectedMinutes,
            long expectedSeconds
    ) {
        Duration expected = Duration
                .ofHours(expectedHours)
                .plusMinutes(expectedMinutes)
                .plusSeconds(expectedSeconds);

        assertEquals(expected, MediaLengthParser.parse(lengthToParse));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // invalid operators
            "-0:00:00",
            "+0:00:10",
            "0:00:-05",
            "0:00:+10",
            "0:-00:00",
            "0:+00:10",

            // trailing or leading spaces
            " 0:0:00",
            "0:00:00 ",
            " 00:00:00 ",
            "0:00 :00",
            "0: 00:00 ",
            "00:00:00 ",
            "00:00: 00",

            // invalid characters
            "01:abc:00",
            "01:1.5:00",
            "01:1,5:00",
            "01:1a:00",
            "02:00:abc",
            "03:00:1.5",
            "04:10:1,5",
            "04:20:1a",
            "abc:00:00",
            "1.5:00:00",
            "1,5:00:00",
            "1a:00:00",

            // empty components
            "::",
            "::00",
            ":1:",
            ":1::",
            ":00:00",
            "0::00",
            "0:00:",

            // malformed
            "0:1:0",
            "1:1:000",
            "2:1:001",
            "0:1:00",
            "0:001:00",
            "1:100:00",
            "2:101:00",

            // leading zeros
            "000:10:50",
            "010:05:15",

            // out of bounds
            "0:00:60",
            "10:00:99",
            "20:10:60",
            "0:60:00",
            "01:99:00",
            "10:60:00"
    })
    void shouldRejectInvalidHoursAndMinutesAndSeconds(String lengthToParse) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2562047788015215:30:08",
            "2562047788015215:30:59",
            "2562047788015216:00:00"
    })
    void shouldRejectHoursAndMinutesAndSecondsOnOverflow(String lengthToParse) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0:00:00:00",
            "1:02:03:04"
    })
    void shouldRejectInvalidNumberOfComponents(String lengthToParse) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse)
        );
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
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(lengthToParse));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "    "})
    void shouldRejectEmptyOrBlankInput(String input) {
        assertThrows(
                MediaLengthParseException.class,
                () -> MediaLengthParser.parse(input)
        );
    }
}
