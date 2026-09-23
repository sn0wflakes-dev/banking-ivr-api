package aji.intern.restapi.service.impl;

import aji.intern.restapi.client.api.OtpClient;
import aji.intern.restapi.client.dto.ApiResponse;
import aji.intern.restapi.client.dto.RequestHeader;
import aji.intern.restapi.client.dto.otp.GenerateOtpApiRequest;
import aji.intern.restapi.client.dto.otp.GenerateOtpApiResponse;
import aji.intern.restapi.client.dto.otp.VerifyOtpApiRequest;
import aji.intern.restapi.client.dto.otp.VerifyOtpApiResponse;
import aji.intern.restapi.config.ApplicationConfig;
import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.otp.GenerateOtpRequest;
import aji.intern.restapi.dto.otp.GenerateOtpResponse;
import aji.intern.restapi.dto.otp.VerifyOtpRequest;
import aji.intern.restapi.dto.otp.VerifyOtpResponse;
import aji.intern.restapi.helper.DateTimeBuilder;
import aji.intern.restapi.service.OtpService;
import aji.intern.restapi.service.ValidationService;
import aji.intern.restapi.utils.SeqNumberUtil;
import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger log = LogManager.getLogger(OtpServiceImpl.class);

    private final ValidationService validationService;
    private final OtpClient otpClient;
    private final ApplicationConfig config;

    public OtpServiceImpl(ValidationService validationService, OtpClient otpClient, ApplicationConfig config) {
        this.validationService = validationService;
        this.otpClient = otpClient;
        this.config = config;
    }

    private RequestHeader requestHeader(MessageHeader messageHeader) {
        return RequestHeader.builder()
                .messageId(messageHeader.getMessageId())
                .serviceId(config.getServiceId())
                .serviceType("IVR")
                .sequenceNumber(SeqNumberUtil.getSeqNumber())
                .transactionDate(messageHeader.getTransactionDate())
                .transactionTime(messageHeader.getTransactionTime())
                .build();
    }

    @Override
    public GenerateOtpResponse generateOtp(MessageHeader messageHeader, GenerateOtpRequest request) {
        validationService.validate(request);
        RequestHeader requestHeader = requestHeader(messageHeader);
        GenerateOtpApiRequest.GenerateOtpApiData data = new GenerateOtpApiRequest.GenerateOtpApiData(request.getCif());
        GenerateOtpApiRequest generateOtpApiRequest = new GenerateOtpApiRequest(requestHeader, data);

        ResponseEntity<ApiResponse<GenerateOtpApiResponse>> response = otpClient.generateOtp(generateOtpApiRequest);

        if (response.getBody() == null) {
            throw new RuntimeException("API response null");
        }

        log.info("Generate OTP success: OTP sent to email");

        return toGenerateOtpResponse(response.getBody().data());
    }

    private GenerateOtpResponse toGenerateOtpResponse(GenerateOtpApiResponse response) {
        return GenerateOtpResponse.builder()
                .email(response.email())
                .expire(response.expire())
                .build();
    }

    @Override
    public VerifyOtpResponse verifyOtp(MessageHeader messageHeader, VerifyOtpRequest request) {
        validationService.validate(request);
        RequestHeader requestHeader = requestHeader(messageHeader);
        VerifyOtpApiRequest.VerifyOtpApiData data =
                new VerifyOtpApiRequest.VerifyOtpApiData(request.getEmailAddress(), request.getOtp());
        VerifyOtpApiRequest verifyOtpApiRequest = new VerifyOtpApiRequest(requestHeader, data);

        ResponseEntity<ApiResponse<VerifyOtpApiResponse>> response = otpClient.verifyOtp(verifyOtpApiRequest);

        if (response.getBody() == null) {
            throw new RuntimeException("API response null");
        }

        log.info("Verify OTP success: OTP is valid");

        return toVerifyOtpResponse(response.getBody().data());
    }

    private VerifyOtpResponse toVerifyOtpResponse(VerifyOtpApiResponse response) {
        return VerifyOtpResponse.builder()
                .email(response.email())
                .status(response.status())
                .build();
    }
}
