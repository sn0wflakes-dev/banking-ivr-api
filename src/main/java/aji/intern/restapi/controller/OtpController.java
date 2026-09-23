package aji.intern.restapi.controller;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.dto.card.AuthenticateCardRequest;
import aji.intern.restapi.dto.card.AuthenticateCardResponse;
import aji.intern.restapi.dto.otp.GenerateOtpRequest;
import aji.intern.restapi.dto.otp.GenerateOtpResponse;
import aji.intern.restapi.dto.otp.VerifyOtpRequest;
import aji.intern.restapi.dto.otp.VerifyOtpResponse;
import aji.intern.restapi.helper.MessageHeaderBuilder;
import aji.intern.restapi.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.OffsetDateTime;

@Controller
@RequestMapping("/api/otp")
public class OtpController {
    private final OtpService service;

    public OtpController(OtpService service) {
        this.service = service;
    }

    @PostMapping(
            path = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<GenerateOtpResponse>> generateOtpEndpoint(
            @RequestBody GenerateOtpRequest request,
            HttpServletRequest httpServletRequest) {

        MessageHeaderBuilder messageHeaderBuilder = new MessageHeaderBuilder(httpServletRequest);
        MessageHeader header = messageHeaderBuilder.messageHeader();

        GenerateOtpResponse response = service.generateOtp(header, request);

        WebResponse<GenerateOtpResponse> apiRes = WebResponse.<GenerateOtpResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(header.getMessageId())
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }

    @PostMapping(
            path = "/verify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<VerifyOtpResponse>> verifyOtpEndpoint(
            @RequestBody VerifyOtpRequest request,
            HttpServletRequest httpServletRequest) {

        MessageHeaderBuilder messageHeaderBuilder = new MessageHeaderBuilder(httpServletRequest);
        MessageHeader header = messageHeaderBuilder.messageHeader();

        VerifyOtpResponse response = service.verifyOtp(header, request);

        WebResponse<VerifyOtpResponse> apiRes = WebResponse.<VerifyOtpResponse>builder()
                .header(WebResponse.ResponseHeader.builder()
                        .requestId(header.getMessageId())
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(response)
                .build();

        return ResponseEntity.ok(apiRes);
    }
}
