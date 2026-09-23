package aji.intern.restapi.service;

import aji.intern.restapi.dto.MessageHeader;
import aji.intern.restapi.dto.otp.GenerateOtpRequest;
import aji.intern.restapi.dto.otp.GenerateOtpResponse;
import aji.intern.restapi.dto.otp.VerifyOtpRequest;
import aji.intern.restapi.dto.otp.VerifyOtpResponse;

public interface OtpService {
    GenerateOtpResponse generateOtp(MessageHeader messageHeader, GenerateOtpRequest request);
    VerifyOtpResponse verifyOtp(MessageHeader messageHeader, VerifyOtpRequest request);
}
