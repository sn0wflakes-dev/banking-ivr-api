package aji.intern.restapi.client.dto.otp;

public record GenerateOtpApiResponse(
        String email,
        String expire
) {
}
