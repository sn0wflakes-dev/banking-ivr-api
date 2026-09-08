package aji.intern.restapi.error;

import aji.intern.restapi.dto.SoapFaultResponse;
import lombok.Getter;

@Getter
public abstract class SoapClientException extends RuntimeException {
    private final String messageId;
    private final String errorOrigin;
    private final String responseCode;
    private final String responseMessage;

    public SoapClientException(SoapFaultResponse soapFaultResponse) {
        super(soapFaultResponse.getResponseMessage());
        this.messageId = soapFaultResponse.getMessageId();
        this.errorOrigin = soapFaultResponse.getErrorOrigin();
        this.responseCode = soapFaultResponse.getResponseCode();
        this.responseMessage = soapFaultResponse.getResponseMessage();
    }
}
