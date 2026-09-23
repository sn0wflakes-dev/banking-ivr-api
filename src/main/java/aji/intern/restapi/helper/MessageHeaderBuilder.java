package aji.intern.restapi.helper;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.filter.MessageHeaderVal;
import jakarta.servlet.http.HttpServletRequest;

public class MessageHeaderBuilder {

    private final HttpServletRequest request;

    public MessageHeaderBuilder(HttpServletRequest request) {
        this.request = request;
    }

    public MessageHeader messageHeader() {
        String messageId = (String) request.getAttribute(MessageHeaderVal.MSG_ID.toString());
        String transactionDate = (String) request.getAttribute(MessageHeaderVal.TDATE.toString());
        String transactionTime = (String) request.getAttribute(MessageHeaderVal.TTIME.toString());

        return MessageHeader.builder()
                .messageId(messageId)
                .transactionDate(transactionDate)
                .transactionTime(transactionTime)
                .build();
    }
}
