package aji.intern.restapi.client.dto;

import lombok.Builder;

@Builder
public record RequestHeader(
        String messageId,
        String serviceId,
        String serviceType,
        String sequenceNumber,
        String transactionDate,
        String transactionTime
) {
}
