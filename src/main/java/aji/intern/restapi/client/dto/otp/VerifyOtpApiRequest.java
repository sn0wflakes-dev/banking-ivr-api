package aji.intern.restapi.client.dto.otp;

import aji.intern.restapi.client.dto.RequestHeader;

public record VerifyOtpApiRequest(
        RequestHeader requestHeader,
        VerifyOtpApiData verifyOtpData
) {
    public record VerifyOtpApiData(
            String email,
            String otp
    ) {}
}
