package aji.intern.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageHeader {
    private String messageId;
    private String transactionDate;
    private String transactionTime;

    public String getMessageId() {
        return messageId != null ? this.messageId : "N/A";
    }

    public String getTransactionDate() {
        return transactionDate != null ? this.transactionDate : "N/A";
    }

    public String getTransactionTime() {
        return transactionTime != null ? this.transactionTime : "N/A";
    }
}
