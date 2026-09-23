package aji.intern.restapi.client.api;

import aji.intern.restapi.client.dto.ApiResponse;
import aji.intern.restapi.client.dto.otp.GenerateOtpApiRequest;
import aji.intern.restapi.client.dto.otp.GenerateOtpApiResponse;
import aji.intern.restapi.client.dto.otp.VerifyOtpApiRequest;
import aji.intern.restapi.client.dto.otp.VerifyOtpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/otp")
public interface OtpClient {
    @PostExchange("/generate")
    ResponseEntity<ApiResponse<GenerateOtpApiResponse>> generateOtp(@RequestBody GenerateOtpApiRequest request);

    @PostExchange("/verify")
    ResponseEntity<ApiResponse<VerifyOtpApiResponse>> verifyOtp(@RequestBody VerifyOtpApiRequest request);
}
