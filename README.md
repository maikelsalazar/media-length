# Media Length

Media Length is a Java library for parsing and formatting media lengths using `java.time.Duration`.

A **media length** is a non-negative duration represented in one of the following formats:

- Seconds (`S+`), for example: `0`, `10`, `180`.
- Minutes and seconds (`M+:SS`), for example: `0:00`, `10:30`, `90:10`, `180:00`.
- Hours, minutes, and seconds (`H+:MM:SS`), for example: `0:00:00`, `1:10:00`, `09:00:00`, `10:30:00`.

> [!NOTE]
> Variable-length components may contain one or two digits with leading zeroes. Values containing three or more digits
> must not begin with zero.
>
> Fixed-width minute and second components must contain exactly two digits and have a value between `00` and `59`.

## Requirements

- Java 21 or later
- Maven 3.9 or later, or the included Maven Wrapper

## Installation

Media Length is not currently published to a public Maven repository.

To install the current snapshot in your local Maven repository, clone the project and run:

```shell
./mvnw clean install
```

Then add the dependency to your project:

```xml
<dependency>
    <groupId>io.github.maikelsalazar</groupId>
    <artifactId>media-length</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
import io.github.maikelsalazar.medialength.MediaLength;

import java.time.Duration;

// Parsing
Duration duration = MediaLength.parse("01:05:09");

System.out.printf(
        "%d:%d:%d%n",
        duration.toHours(),
        duration.toMinutesPart(),
        duration.toSecondsPart()
); // Output: 1:5:9

// Formatting
System.out.println(MediaLength.format(duration)); // Output: 1:05:09
```

## API

### `MediaLength.parse(String)`

Parses a media-length string into a `Duration`.

**Signature**

```java
public static Duration parse(String mediaLength);
```

#### Allowed formats

In the notation below, `+` means “one or more digits”.

| Description                 | Format     | Input examples                                 | Restrictions                                                                                                                                                             |
|-----------------------------|------------|------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Seconds only                | `S+`       | `9`, `30`, `180`                               | Seconds must contain at least one digit. The resulting value must fit within `Duration`.                                                                                 |
| Minutes and seconds         | `M+:SS`    | `0:30`, `00:59`, `20:30`, `90:30`, `100:30`    | Minutes must contain at least one digit. Seconds must contain exactly two digits and be between `00` and `59`. The resulting value must fit within `Duration`.           |
| Hours, minutes, and seconds | `H+:MM:SS` | `1:20:30`, `01:20:30`, `10:20:30`, `100:20:30` | Hours must contain at least one digit. Minutes and seconds must contain exactly two digits and be between `00` and `59`. The resulting value must fit within `Duration`. |

Variable-length components follow these rules:

- One- and two-digit values may contain leading zeroes, such as `0`, `00`, `01`, and `09`.
- Values containing three or more digits must not begin with zero. For example, `100` is valid, but `010` is not.

#### Parsing examples

| Input         | Result                                |
|---------------|---------------------------------------|
| `"0"`         | `Duration.ZERO`                       |
| `"59"`        | 59 seconds                            |
| `"1:00"`      | 1 minute                              |
| `"90:30"`     | 90 minutes and 30 seconds             |
| `"1:20:30"`   | 1 hour, 20 minutes, and 30 seconds    |
| `"24:00:00"`  | 24 hours                              |
| `"100:10:59"` | 100 hours, 10 minutes, and 59 seconds |

#### Input restrictions

- `null`, empty, and blank inputs are not allowed.
- Empty components are not allowed.
- Leading, trailing, or internal whitespace is not allowed.
- Only ASCII digits (`0-9`) and the colon separator (`:`) are allowed.
- Letters, decimal separators, and signs such as `-` and `+` are not allowed.
- Values must fit within the range supported by `Duration`.

#### Invalid input examples

| Input       | Reason                                        |
|-------------|-----------------------------------------------|
| `" 30"`     | Leading whitespace                            |
| `"30 "`     | Trailing whitespace                           |
| `"1: 30"`   | Whitespace inside a component                 |
| `"+30"`     | Explicit positive sign                        |
| `"-30"`     | Negative sign                                 |
| `"010"`     | Leading zero in a component with three digits |
| `"1.30"`    | Decimal separator                             |
| `"1m30s"`   | Explicit unit symbols                         |
| `":30"`     | Empty component                               |
| `"30:"`     | Empty component                               |
| `"1::30"`   | Empty component                               |
| `"1:2"`     | Seconds are not exactly two digits            |
| `"1:60"`    | Seconds are outside the supported range       |
| `"1:60:00"` | Minutes are outside the supported range       |
| `"1:00:60"` | Seconds are outside the supported range       |

#### Unsupported representations

The parser does not support:

- Fractional seconds, including milliseconds and nanoseconds
- A separate days component or day symbol
- Explicit unit symbols such as `h`, `m`, and `s`
- ISO-8601 duration representations
- More than three components
- Any other time unit or component

Durations longer than 24 hours remain supported when expressed using total hours, such as `48:00:00`.

| Input          | Reason                  |
|----------------|-------------------------|
| `"1.500"`      | Fractional seconds      |
| `"1:30.500"`   | Fractional seconds      |
| `"2d"`         | Day symbol              |
| `"1h30m"`      | Explicit unit symbols   |
| `"PT1H30M"`    | ISO-8601 representation |
| `"1:02:03:04"` | Too many components     |

#### Parsing errors

`MediaLength.parse` throws `MediaLengthParseException` when the input:

- Is `null`, empty, or blank
- Does not match a supported format
- Contains an invalid component
- Contains a value outside the supported range

```java
import io.github.maikelsalazar.medialength.MediaLength;
import io.github.maikelsalazar.medialength.MediaLengthParseException;

try {
    MediaLength.parse("1:60");
} catch (MediaLengthParseException exception) {
    System.out.println(exception.getMessage());
}
```

`MediaLengthParseException` extends `MediaLengthException`, which extends `IllegalArgumentException`.

---

### `MediaLength.format(Duration)`

Formats a non-negative `Duration` as a media-length string.

**Signature**

```java
public static String format(Duration duration);
```

#### Formatting rules
The shortest applicable format is selected:

| Duration                               | Output format | Example                              |
|----------------------------------------|---------------|--------------------------------------|
| Less than 1 minute                     | `S+`          | 30 seconds → `"30"`                  |
| At least 1 minute and less than 1 hour | `M+:SS`       | 10 minutes and 5 seconds → `"10:05"` |
| At least 1 hour                        | `H+:MM:SS`    | 2 hours and 5 seconds → `"2:00:05"`  |

Formatting produces a canonical representation:

- Variable-length components do not contain unnecessary leading zeroes.
- Fixed-width components always contain exactly two digits.
- Durations of 24 hours or longer are represented using total hours.
- Fractional seconds are truncated rather than rounded.

#### Formatting examples

| Duration                                             | Result       |
|------------------------------------------------------|--------------|
| `Duration.ZERO`                                      | `"0"`        |
| `Duration.ofSeconds(59)`                             | `"59"`       |
| `Duration.ofMinutes(1)`                              | `"1:00"`     |
| `Duration.ofMinutes(59).plusSeconds(1)`              | `"59:01"`    |
| `Duration.ofHours(1)`                                | `"1:00:00"`  |
| `Duration.ofHours(25).plusMinutes(1).plusSeconds(2)` | `"25:01:02"` |
| `Duration.ofSeconds(1).plusNanos(500_000_000)`       | `"1"`        |

Because formatting uses the shortest applicable format, parsing and formatting may normalize the original
representation:

```java
MediaLength.format(MediaLength.parse("01:05:09"));
// "1:05:09"

MediaLength.format(MediaLength.parse("90:10"));
// "1:30:10"

MediaLength.format(MediaLength.parse("00"));
// "0"
```

#### Formatting errors

`MediaLength.format` throws `MediaLengthFormatException` when the supplied duration is:

- `null`
- Negative

```java
import io.github.maikelsalazar.medialength.MediaLength;
import io.github.maikelsalazar.medialength.MediaLengthFormatException;

import java.time.Duration;

try {
    MediaLength.format(Duration.ofSeconds(-1));
} catch (MediaLengthFormatException exception) {
    System.out.println(exception.getMessage());
}
```

`MediaLengthFormatException` extends `MediaLengthException`, which extends `IllegalArgumentException`.

## Exception hierarchy

```text
IllegalArgumentException
└── MediaLengthException
    ├── MediaLengthParseException
    └── MediaLengthFormatException
```

You can catch the specific parsing or formatting exception, or catch `MediaLengthException` to handle both:

```java
import io.github.maikelsalazar.medialength.MediaLength;
import io.github.maikelsalazar.medialength.MediaLengthException;

import java.time.Duration;

// Parsing
try {
    MediaLength.parse("-1");
} catch (MediaLengthException exception) {
    System.out.println(exception.getMessage());
}

// Formatting
try {
    MediaLength.format(Duration.ofSeconds(-1));
} catch (MediaLengthException exception) {
    System.out.println(exception.getMessage());
}
```

## Building from source

Clone the repository:

```shell
git clone https://github.com/maikelsalazar/media-length.git
cd media-length
```

Compile and run the tests:

```shell
./mvnw clean verify
```

Install the snapshot in your local Maven repository:

```shell
./mvnw clean install
```

## License

Media Length is licensed under the [Apache License 2.0](LICENSE).
