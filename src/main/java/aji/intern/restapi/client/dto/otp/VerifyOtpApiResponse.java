package aji.intern.restapi.client.dto.otp;

public record VerifyOtpApiResponse(
        String email,
        String status
) {
}
