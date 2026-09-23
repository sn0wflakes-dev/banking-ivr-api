package aji.intern.restapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeqNumberUtilTest {
    @Test
    void shouldReturnRightFormatOfSequenceNumber() {
        String seqNumber = SeqNumberUtil.getSeqNumber();

        Assertions.assertNotNull(seqNumber);
        Assertions.assertEquals(6, seqNumber.length());
        Assertions.assertTrue(seqNumber.matches("\\d{6}"));
    }

    @Test
    void shouldAlwaysReturnSixDigit() {
        for (int i = 0; i < 100; i++) {
            String sequenceNumber = SeqNumberUtil.getSeqNumber();

            Assertions.assertEquals(6, sequenceNumber.length());
            Assertions.assertTrue(sequenceNumber.matches("\\d{6}"));
        }
    }

    @Test
    void shouldGiveValidRange() {
        String sequenceNumber = SeqNumberUtil.getSeqNumber();

        int value = Integer.parseInt(sequenceNumber);

        Assertions.assertTrue(value >= 0);
        Assertions.assertTrue(value <= 999999);
    }
}
