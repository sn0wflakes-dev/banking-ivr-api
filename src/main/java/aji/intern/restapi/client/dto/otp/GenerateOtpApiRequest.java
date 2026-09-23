package aji.intern.restapi.client.dto.otp;

import aji.intern.restapi.client.dto.RequestHeader;

public record GenerateOtpApiRequest(
        RequestHeader requestHeader,
        GenerateOtpApiData generateOtpData
) {
    public record GenerateOtpApiData(
            String cif
    ) {}
}
