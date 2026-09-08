package aji.intern.restapi.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public final ZoneId timezone = ZoneId.of("Asia/Jakarta");
    public final DateTimeFormatter transactionTime = DateTimeFormatter.ofPattern("hh:mm:ss").withZone(timezone);
    public final DateTimeFormatter transactionDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(timezone);

    public DateTimeUtil() {}

    public String getTransactionTime() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(transactionTime);
    }

    public String getTransactionDate() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(transactionDate);
    }
}
