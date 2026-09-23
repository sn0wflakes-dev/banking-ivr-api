package aji.intern.restapi.service.impl;

import aji.intern.core.bank.account.*;
import aji.intern.restapi.config.SoapServiceConfig;
import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.dto.account.UpdateEmailRequest;
import aji.intern.restapi.dto.account.UpdateEmailResponse;
import aji.intern.restapi.error.exception.soap.AccountServiceException;
import aji.intern.restapi.service.AccountService;
import aji.intern.restapi.service.ValidationService;
import aji.intern.restapi.utils.SeqNumberUtil;
import aji.intern.restapi.utils.XmlParserUtil;
import jakarta.xml.ws.soap.SOAPFaultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    private final SoapServiceConfig soapServiceConfig;
    private final ValidationService validationService;

    public AccountServiceImpl(SoapServiceConfig soapServiceConfig, ValidationService validationService) {
        this.soapServiceConfig = soapServiceConfig;
        this.validationService = validationService;
    }

    private RequestHeader requestHeader(MessageHeader header) {
        ObjectFactory factory = new ObjectFactory();
        RequestHeader requestHeader = factory.createRequestHeader();
        requestHeader.setMessageId(header.getMessageId());
        requestHeader.setServiceType("IVR");
        requestHeader.setSequenceNumber(SeqNumberUtil.getSeqNumber());
        requestHeader.setTransactionDate(header.getTransactionDate());
        requestHeader.setTransactionTime(header.getTransactionTime());
        return requestHeader;
    }

    @Override
    public UpdateEmailResponse updateCustomerEmail(MessageHeader header, UpdateEmailRequest request) {
        validationService.validate(request);
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            UpdateCustomerEmailRequestData updateEmailData = objectFactory.createUpdateCustomerEmailRequestData();
            updateEmailData.setEmail(request.getEmailAddress());
            updateEmailData.setCif(request.getCif());

            UpdateCustomerEmailRequest updateEmailReq = objectFactory.createUpdateCustomerEmailRequest();
            updateEmailReq.setUpdateCustomerEmailData(updateEmailData);
            updateEmailReq.setRequestHeader(requestHeader(header));

            UpdateCustomerEmailResponse updateCustomerEmailResponse = soapServiceConfig
                    .accountService()
                    .updateCustomerEmail(updateEmailReq);

            log.info("Update email success: Customer email updated successfully, with cif={}",
                    request.getCif());

            return toUpdateCustomerEmailRes(updateCustomerEmailResponse);

        } catch (SOAPFaultException e) {
            SoapFaultResponse fault = XmlParserUtil.xmlFaultParser(e.getFault().getDetail());
            throw new AccountServiceException(fault);
        } catch (Exception e) {
            log.error("ERROR : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private UpdateEmailResponse toUpdateCustomerEmailRes(UpdateCustomerEmailResponse response) {
        return UpdateEmailResponse.builder()
                .responseCode(response.getResponseHeader().getResponseCode())
                .message(response.getResponseHeader().getResponseMessage())
                .updateEmail(response.getUpdateCustomerEmailData().getUpdatedEmail())
                .build();
    }
}
