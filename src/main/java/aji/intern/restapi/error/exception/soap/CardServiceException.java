package aji.intern.restapi.error.exception.soap;

import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.error.SoapClientException;

public class CardServiceException extends SoapClientException {
    public CardServiceException(SoapFaultResponse faultResponse) {
        super(faultResponse);
    }
}
