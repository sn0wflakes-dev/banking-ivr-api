package aji.intern.restapi.controller;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.dto.card.ActivateCardNumberRequest;
import aji.intern.restapi.dto.card.ActivateCardNumberResponse;
import aji.intern.restapi.dto.card.AuthenticateCardRequest;
import aji.intern.restapi.dto.card.AuthenticateCardResponse;
import aji.intern.restapi.filter.MessageHeaderVal;
import aji.intern.restapi.helper.MessageHeaderBuilder;
import aji.intern.restapi.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.OffsetDateTime;

@Controller
@RequestMapping("/api/card")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<AuthenticateCardResponse>> authenticateCardEndpoint(
            @RequestBody AuthenticateCardRequest request,
            HttpServletRequest httpServletRequest) {

        MessageHeaderBuilder messageHeaderBuilder = new MessageHeaderBuilder(httpServletRequest);
        MessageHeader header = messageHeaderBuilder.messageHeader();

        AuthenticateCardResponse response = service.authCard(header, request);

        WebResponse<AuthenticateCardResponse> apiRes = WebResponse.<AuthenticateCardResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(header.getMessageId())
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<ActivateCardNumberResponse>> activateCardEndpoint(
            @RequestBody ActivateCardNumberRequest request,
            HttpServletRequest httpServletRequest) {

        MessageHeaderBuilder messageHeaderBuilder = new MessageHeaderBuilder(httpServletRequest);
        MessageHeader header = messageHeaderBuilder.messageHeader();

        ActivateCardNumberResponse response = service.activateCard(header, request);

        WebResponse<ActivateCardNumberResponse> apiRes = WebResponse.<ActivateCardNumberResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(header.getMessageId())
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }
}
