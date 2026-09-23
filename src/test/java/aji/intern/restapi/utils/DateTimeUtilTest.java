package aji.intern.restapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateTimeUtilTest {

    @Test
    void shouldGiveDateTimeValue() {
        // Arrange
        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        // Test
        Assertions.assertNotNull(dateTimeUtil.getTransactionDate());
        Assertions.assertNotNull(dateTimeUtil.getTransactionTime());
    }

    @Test
    void shouldGiveRightDateFormat() {
        String dateFormat = "dd-MM-uuuu";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat)
                .withZone(ZoneId.of("Asia/Jakarta"))
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        Assertions.assertDoesNotThrow(() -> {
                LocalDate.parse(dateTimeUtil.getTransactionDate(), formatter);
        });
    }

    @Test
    void shouldGiveRightTimeFormat() {
        String timeFormat = "HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat)
                .withZone(ZoneId.of("Asia/Jakarta"))
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeUtil dateTimeUtil = new DateTimeUtil();

        Assertions.assertDoesNotThrow(() -> {
            LocalTime.parse(dateTimeUtil.getTransactionTime(), formatter);
        });
    }

}
