package aji.intern.restapi.service.impl;

import aji.intern.core.bank.account.*;
import aji.intern.restapi.config.SoapServiceConfig;
import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.dto.account.UpdateAccountRequest;
import aji.intern.restapi.dto.account.UpdateAccountResponse;
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

    private UpdateAccountResponse toUpdateCustomerEmailRes(UpdateCustomerEmailResponse response) {
        return UpdateAccountResponse.builder()
                .updateEmail(response.getUpdateCustomerEmailData().getUpdatedEmail())
                .build();
    }

    private UpdateAccountResponse toUpdateMobileNumberRes(UpdatePhoneNumberResponse response) {
        return UpdateAccountResponse.builder()
                .updatedPhoneNumber(response.getUpdatePhoneNumberData().getUpdatedPhoneNumber())
                .build();
    }

    @Override
    public UpdateAccountResponse updateAccount(MessageHeader header, UpdateAccountRequest request) {
        validationService.validate(request);
        try {
            ObjectFactory objectFactory = new ObjectFactory();

            if (request.getEmailAddress() != null) {
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

            }

            UpdatePhoneNumberData data = objectFactory.createUpdatePhoneNumberData();
            data.setCif(request.getCif());
            data.setPhoneNumber(request.getPhoneNumber());
            UpdatePhoneNumberRequest phoneNumberRequest = objectFactory.createUpdatePhoneNumberRequest();
            phoneNumberRequest.setUpdatePhoneNumberData(data);
            phoneNumberRequest.setRequestHeader(requestHeader(header));

            UpdatePhoneNumberResponse response = soapServiceConfig.accountService().updatePhoneNumber(phoneNumberRequest);

            log.info("Update phone number success: Customer phone number updated successfully, with cif={}",
                    request.getCif());

            return toUpdateMobileNumberRes(response);
        } catch (SOAPFaultException e) {
            SoapFaultResponse fault = XmlParserUtil.xmlFaultParser(e.getFault().getDetail());
            throw new AccountServiceException(fault);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
