package aji.intern.restapi.service.impl;

import aji.intern.core.bank.card.*;
import aji.intern.restapi.config.ApplicationConfig;
import aji.intern.restapi.config.SoapServiceConfig;
import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.dto.card.ActivateCardNumberRequest;
import aji.intern.restapi.dto.card.ActivateCardNumberResponse;
import aji.intern.restapi.dto.card.AuthenticateCardRequest;
import aji.intern.restapi.dto.card.AuthenticateCardResponse;
import aji.intern.restapi.error.exception.soap.CardServiceException;
import aji.intern.restapi.helper.RsaCrypto;
import aji.intern.restapi.service.CardService;
import aji.intern.restapi.service.ValidationService;
import aji.intern.restapi.utils.SeqNumberUtil;
import aji.intern.restapi.utils.XmlParserUtil;
import jakarta.xml.ws.soap.SOAPFaultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private static final Logger log = LogManager.getLogger(CardServiceImpl.class);

    private final SoapServiceConfig soapServiceConfig;
    private final ValidationService validationService;
    private final ApplicationConfig applicationConfig;

    public CardServiceImpl(
            SoapServiceConfig soapServiceConfig,
            ValidationService validationService,
            ApplicationConfig applicationConfig) {
        this.soapServiceConfig = soapServiceConfig;
        this.validationService = validationService;
        this.applicationConfig = applicationConfig;
    }

    private RequestHeader requestHeader(MessageHeader header) {
        ObjectFactory factory = new ObjectFactory();
        RequestHeader requestHeader = factory.createRequestHeader();
        requestHeader.setMessageId(header.getMessageId());
        requestHeader.setServiceId(applicationConfig.getServiceId());
        requestHeader.setServiceType("IVR");
        requestHeader.setSequenceNumber(SeqNumberUtil.getSeqNumber());
        requestHeader.setTransactionDate(header.getTransactionDate());
        requestHeader.setTransactionTime(header.getTransactionTime());
        return requestHeader;
    }

    @Override
    public AuthenticateCardResponse authCard(MessageHeader messageHeader, AuthenticateCardRequest request) {
        validationService.validate(request);
        try {
            String encryptedPin = RsaCrypto.encrypt(request.getPin(), applicationConfig.getServiceKey());
            ObjectFactory objectFactory = new ObjectFactory();
            AuthCardData data = objectFactory.createAuthCardData();
            data.setCardNumber(request.getCardNumber());
            data.setPin(encryptedPin);
            AuthCardRequest authCardRequest = objectFactory.createAuthCardRequest();
            authCardRequest.setAuthCardData(data);
            authCardRequest.setRequestHeader(requestHeader(messageHeader));

            AuthCardResponse response = soapServiceConfig.cardService().authCard(authCardRequest);
            return toAuthenticateCardRes(response);
        } catch (SOAPFaultException e) {
            SoapFaultResponse fault = XmlParserUtil.xmlFaultParser(e.getFault().getDetail());
            throw new CardServiceException(fault);
        } catch (Exception e) {
            log.error("ERROR : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private AuthenticateCardResponse toAuthenticateCardRes(AuthCardResponse response) {
        return AuthenticateCardResponse.builder()
                .customerNumber(response.getAuthCardData().getCustomerNumber())
                .name(response.getAuthCardData().getName())
                .address(response.getAuthCardData().getAddress())
                .cif(response.getAuthCardData().getCif())
                .cardNumber(response.getAuthCardData().getCardNumber())
                .build();
    }

    @Override
    public ActivateCardNumberResponse activateCard(MessageHeader messageHeader, ActivateCardNumberRequest request) {
        validationService.validate(request);
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            ActivateCardData data = objectFactory.createActivateCardData();
            data.setCardNumber(request.getCardNumber());
            ActivateCardRequest activateCardRequest = objectFactory.createActivateCardRequest();
            activateCardRequest.setActivateCardData(data);
            activateCardRequest.setRequestHeader(requestHeader(messageHeader));

            ActivateCardResponse response = soapServiceConfig.cardService().activateCard(activateCardRequest);
            return toActivateCardRes(response);
        } catch (SOAPFaultException e) {
            SoapFaultResponse fault = XmlParserUtil.xmlFaultParser(e.getFault().getDetail());
            throw new CardServiceException(fault);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ActivateCardNumberResponse toActivateCardRes(ActivateCardResponse response) {
        return ActivateCardNumberResponse.builder()
                .cardNumber(response.getActivateCardData().getCardNumber())
                .status(response.getActivateCardData().getStatus())
                .build();
    }

}
