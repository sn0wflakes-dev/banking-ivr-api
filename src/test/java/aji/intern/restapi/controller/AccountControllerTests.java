package aji.intern.restapi.controller;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.SoapFaultResponse;
import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.dto.account.UpdateEmailRequest;
import aji.intern.restapi.dto.account.UpdateEmailResponse;
import aji.intern.restapi.error.SoapClientException;
import aji.intern.restapi.service.AccountService;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private final static String cif = UuidCreator.getTimeOrderedEpoch().toString();
    private final static String emailAddress = "abdurrahmanali.dev@gmail.com";

    @Test
    void shouldUpdateEmail() throws Exception {
        // Arrange
        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(cif)
                .emailAddress(emailAddress)
                .build();

        UpdateEmailResponse response = UpdateEmailResponse.builder()
                .responseCode("00")
                .updateEmail(emailAddress)
                .message("success")
                .build();

        // Mock
        Mockito.when(accountService.updateCustomerEmail(any(), any(UpdateEmailRequest.class))).thenReturn(response);


        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.responseCode").value("00"))
                .andExpect(jsonPath("$.data.message").value("success"))
                .andExpect(jsonPath("$.data.updateEmail").value(emailAddress));


        ArgumentCaptor<MessageHeader> messageHeaderCaptor = ArgumentCaptor.forClass(MessageHeader.class);
        ArgumentCaptor<UpdateEmailRequest> requestCaptor = ArgumentCaptor.forClass(UpdateEmailRequest.class);

        // Test
        verify(accountService).updateCustomerEmail(messageHeaderCaptor.capture(), requestCaptor.capture());

        Assertions.assertNotNull(messageHeaderCaptor.getValue().getMessageId());
        Assertions.assertNotNull(messageHeaderCaptor.getValue().getTransactionDate());
        Assertions.assertNotNull(messageHeaderCaptor.getValue().getTransactionDate());

        Assertions.assertEquals(cif, requestCaptor.getValue().getCif());
        Assertions.assertEquals(emailAddress, requestCaptor.getValue().getEmailAddress());
    }

    @Test
    void shouldReturn400BadRequest_whenCifIsNull() throws Exception {
        // Arrange
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("cif");

        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(null)
                .emailAddress(emailAddress)
                .build();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        when(violation.getPropertyPath())
                .thenReturn(path);

        when(violation.getMessage())
                .thenReturn("must not be blank");

        ConstraintViolationException exception =
                new ConstraintViolationException(
                        Set.of(violation)
                );

        when(accountService.updateCustomerEmail(any(), any()))
                .thenThrow(exception);

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});
                    Assertions.assertNotNull(response.getError());
                });
    }

    @Test
    void shouldReturn400BadRequest_whenCifIsEmpty() throws Exception {
        // Arrange
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("cif");

        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif("")
                .emailAddress(emailAddress)
                .build();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        when(violation.getPropertyPath())
                .thenReturn(path);

        when(violation.getMessage())
                .thenReturn("must not be blank");

        ConstraintViolationException exception =
                new ConstraintViolationException(
                        Set.of(violation)
                );

        when(accountService.updateCustomerEmail(any(), any()))
                .thenThrow(exception);

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});
                    Assertions.assertNotNull(response.getError());
                });
    }

    @Test
    void shouldReturn400BadRequest_whenEmailIsNull() throws Exception {
        // Arrange
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("emailAddress");

        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(cif)
                .emailAddress(null)
                .build();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        when(violation.getPropertyPath())
                .thenReturn(path);

        when(violation.getMessage())
                .thenReturn("must not be blank");

        ConstraintViolationException exception =
                new ConstraintViolationException(
                        Set.of(violation)
                );

        when(accountService.updateCustomerEmail(any(), any()))
                .thenThrow(exception);

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});
                    Assertions.assertNotNull(response.getError());
                });
    }

    @Test
    void shouldReturn400BadRequest_whenEmailIsEmpty() throws Exception {
        // Arrange
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("emailAddress");

        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(cif)
                .emailAddress("")
                .build();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        when(violation.getPropertyPath())
                .thenReturn(path);

        when(violation.getMessage())
                .thenReturn("must not be blank");

        ConstraintViolationException exception =
                new ConstraintViolationException(
                        Set.of(violation)
                );

        when(accountService.updateCustomerEmail(any(), any()))
                .thenThrow(exception);

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});
                    Assertions.assertNotNull(response.getError());
                });
    }


    @Test
    void shouldReturn404NotFound() throws Exception {
        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(cif)
                .emailAddress(emailAddress)
                .build();

        SoapFaultResponse response = new SoapFaultResponse();
        response.setMessageId("MSG-123");
        response.setErrorOrigin("ws");
        response.setResponseCode("04");
        response.setResponseMessage("Cif not found");

        SoapClientException exception =
                new SoapClientException(response) {
                };

        when(accountService.updateCustomerEmail(any(), any())).thenThrow(exception);

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.responseCode").value("04"))
                .andExpect(jsonPath("$.error.errorOrigin").value("ws"))
                .andExpect(jsonPath("$.error.message").value("Cif not found"));

    }

    @Test
    void shouldReturn500InternalServerError() throws Exception {
        UpdateEmailRequest request = UpdateEmailRequest.builder()
                .cif(cif)
                .emailAddress(emailAddress)
                .build();

        when(accountService.updateCustomerEmail(any(), any())).thenThrow(new RuntimeException("Something went wrong"));

        // Act
        mockMvc.perform(
                        patch("/api/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))

                )
                // Test
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error.responseCode").value("99"))
                .andExpect(jsonPath("$.error.errorOrigin").value("IVR-API"))
                .andExpect(jsonPath("$.error.message").value("500 Internal Server Error"));
    }
}
