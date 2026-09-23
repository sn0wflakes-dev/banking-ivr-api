package aji.intern.restapi.utils;

import aji.intern.restapi.helper.DateTimeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateTimeBuilderTest {

    @Test
    void shouldGiveDateTimeValue() {
        // Arrange
        DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();

        // Test
        Assertions.assertNotNull(dateTimeBuilder.getTransactionDate());
        Assertions.assertNotNull(dateTimeBuilder.getTransactionTime());
    }

    @Test
    void shouldGiveRightDateFormat() {
        String dateFormat = "dd-MM-uuuu";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat)
                .withZone(ZoneId.of("Asia/Jakarta"))
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();

        Assertions.assertDoesNotThrow(() -> {
                LocalDate.parse(dateTimeBuilder.getTransactionDate(), formatter);
        });
    }

    @Test
    void shouldGiveRightTimeFormat() {
        String timeFormat = "HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat)
                .withZone(ZoneId.of("Asia/Jakarta"))
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();

        Assertions.assertDoesNotThrow(() -> {
            LocalTime.parse(dateTimeBuilder.getTransactionTime(), formatter);
        });
    }

}
