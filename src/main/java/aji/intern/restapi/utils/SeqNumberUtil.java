package aji.intern.restapi.utils;

import java.time.LocalDateTime;

public class SeqNumberUtil {
    public static String getSeqNumber() {
        LocalDateTime now = LocalDateTime.now();
        long nanos = System.nanoTime() % 100000;

        long value = (now.getHour() * 10000 + now.getMinute() * 100 + now.getSecond()) * 1000 + (nanos / 1000);

        return String.format("%06d", value % 1000000);
    }
}
