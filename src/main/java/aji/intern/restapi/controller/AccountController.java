package aji.intern.restapi.controller;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.dto.account.UpdateAccountRequest;
import aji.intern.restapi.dto.account.UpdateAccountResponse;
import aji.intern.restapi.helper.MessageHeaderBuilder;
import aji.intern.restapi.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<UpdateAccountResponse>> updateAccountEndpoint(
            @RequestBody UpdateAccountRequest request,
            HttpServletRequest httpServletRequest) {

        MessageHeaderBuilder messageHeaderBuilder = new MessageHeaderBuilder(httpServletRequest);
        MessageHeader header = messageHeaderBuilder.messageHeader();

        UpdateAccountResponse response = service.updateAccount(header, request);

        WebResponse<UpdateAccountResponse> apiRes = WebResponse.<UpdateAccountResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(header.getMessageId())
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }

}
