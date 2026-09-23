package aji.intern.restapi.filter;

import lombok.Getter;

@Getter
public enum MessageHeaderVal {
    MSG_ID("messageId"),
    TTIME("transactionTime"),
    TDATE("transactionDate");

    private final String ctx;

    MessageHeaderVal(String ctx) {
        this.ctx = ctx;
    }

    @Override
    public String toString() {
        return this.ctx;
    }
}
