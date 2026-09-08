package aji.intern.restapi.error.exception.soap;

import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.error.SoapClientException;

public class AccountServiceException extends SoapClientException {
    public AccountServiceException(SoapFaultResponse faultResponse) {
        super(faultResponse);
    }
}
