package aji.intern.restapi.controller;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.dto.account.UpdateEmailRequest;
import aji.intern.restapi.dto.account.UpdateEmailResponse;
import aji.intern.restapi.filter.MessageHeaderVal;
import aji.intern.restapi.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<UpdateEmailResponse>> updateEmailEndpoint(
            @RequestBody UpdateEmailRequest request,
            HttpServletRequest httpServletRequest) {

        String messageId = (String) httpServletRequest.getAttribute(MessageHeaderVal.MSG_ID.toString());
        String transactionDate = (String) httpServletRequest.getAttribute(MessageHeaderVal.TDATE.toString());
        String transactionTime = (String) httpServletRequest.getAttribute(MessageHeaderVal.TTIME.toString());

        MessageHeader header = MessageHeader.builder()
                .messageId(messageId)
                .transactionDate(transactionDate)
                .transactionTime(transactionTime)
                .build();

        UpdateEmailResponse response = service.updateCustomerEmail(header, request);

        WebResponse<UpdateEmailResponse> apiRes = WebResponse.<UpdateEmailResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(messageId)
                        .timestamp(Instant.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }

}
