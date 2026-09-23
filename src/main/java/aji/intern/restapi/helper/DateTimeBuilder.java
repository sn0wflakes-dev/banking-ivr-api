package aji.intern.restapi.helper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeBuilder {
    public final ZoneId timezone = ZoneId.of("Asia/Jakarta");
    public final DateTimeFormatter transactionTime = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(timezone);
    public final DateTimeFormatter transactionDate = DateTimeFormatter.ofPattern("dd-MM-uuuu").withZone(timezone);

    public DateTimeBuilder() {}

    public String getTransactionTime() {
        LocalTime time = LocalTime.now();
        return time.format(transactionTime);
    }

    public String getTransactionDate() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(transactionDate);
    }
}
