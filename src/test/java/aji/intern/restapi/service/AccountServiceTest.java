package aji.intern.restapi.service;

import aji.intern.core.bank.account.*;
import aji.intern.restapi.config.SoapServiceConfig;
import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.account.UpdateEmailRequest;
import aji.intern.restapi.service.impl.AccountServiceImpl;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private SoapServiceConfig soapServiceConfig;

    @Mock
    private Account accountService;

    @Mock
    private ValidationService validationService;

    private AccountServiceImpl accountServiceImpl;

    @BeforeEach
    void setUp() {
        accountServiceImpl = new AccountServiceImpl(soapServiceConfig, validationService);

        when(soapServiceConfig.accountService())
                .thenReturn(accountService);
    }

    @Test
    void shouldBuildCorrectRequest() {
        MessageHeader header = MessageHeader.builder()
                .messageId("MSG-001")
                .transactionDate("20260923")
                .transactionTime("040000")
                .build();

        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif("123456789")
                .emailAddress("new.email@example.com")
                .build();

        UpdateCustomerEmailResponse response = getUpdateCustomerEmailResponse();

        when(accountService.updateCustomerEmail(any()))
                .thenReturn(response);

        // Act
        accountServiceImpl.updateCustomerEmail(header, request);

        // Assert
        ArgumentCaptor<UpdateCustomerEmailRequest> captor =
                ArgumentCaptor.forClass(UpdateCustomerEmailRequest.class);

        verify(accountService).updateCustomerEmail(captor.capture());

        UpdateCustomerEmailRequest soapRequest = captor.getValue();

        assertNotNull(soapRequest);

        // Header
        RequestHeader soapHeader = soapRequest.getRequestHeader();

        assertNotNull(soapHeader);
        assertEquals("MSG-001", soapHeader.getMessageId());
        assertEquals("IVR", soapHeader.getServiceType());
        assertEquals("20260923", soapHeader.getTransactionDate());
        assertEquals("040000", soapHeader.getTransactionTime());

        // Data
        UpdateCustomerEmailRequestData data =
                soapRequest.getUpdateCustomerEmailData();

        assertNotNull(data);
        assertEquals("123456789", data.getCif());
        assertEquals("new.email@example.com", data.getEmail());
    }

    private static @NonNull UpdateCustomerEmailResponse getUpdateCustomerEmailResponse() {
        ObjectFactory factory = new ObjectFactory();

        ResponseHeader responseHeader = factory.createResponseHeader();
        responseHeader.setResponseCode("00");
        responseHeader.setResponseMessage("SUCCESS");

        UpdateCustomerEmailResponse response =
                factory.createUpdateCustomerEmailResponse();

        response.setResponseHeader(responseHeader);

        UpdateCustomerEmailResponseData responseData =
                factory.createUpdateCustomerEmailResponseData();

        responseData.setUpdatedEmail("new.email@example.com");

        response.setUpdateCustomerEmailData(responseData);
        return response;
    }
}
