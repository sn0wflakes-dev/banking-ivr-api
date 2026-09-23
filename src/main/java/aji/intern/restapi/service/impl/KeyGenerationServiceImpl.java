package aji.intern.restapi.service.impl;

import aji.intern.restapi.client.api.KeyClient;
import aji.intern.restapi.client.dto.ApiResponse;
import aji.intern.restapi.client.dto.RequestHeader;
import aji.intern.restapi.client.dto.key.*;
import aji.intern.restapi.config.ApplicationConfig;
import aji.intern.restapi.service.KeyGenerationService;
import aji.intern.restapi.helper.DateTimeBuilder;
import aji.intern.restapi.utils.SeqNumberUtil;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KeyGenerationServiceImpl implements KeyGenerationService {

    private final KeyClient client;
    private final ApplicationConfig config;

    public KeyGenerationServiceImpl(KeyClient client, ApplicationConfig config) {
        this.client = client;
        this.config = config;
    }

    private RequestHeader requestHeader() {
        DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();
        return RequestHeader.builder()
                .messageId(UuidCreator.getTimeOrderedEpoch().toString())
                .serviceId(config.getServiceId())
                .serviceType("IVR")
                .sequenceNumber(SeqNumberUtil.getSeqNumber())
                .transactionDate(dateTimeBuilder.getTransactionDate())
                .transactionTime(dateTimeBuilder.getTransactionTime())
                .build();
    }

    @Override
    public String registerServiceKey() {
        RequestHeader requestHeader = requestHeader();
        RegisterServiceApiRequest.RegisterServiceApiData data =
                new RegisterServiceApiRequest.RegisterServiceApiData(config.getServiceId());

        RegisterServiceApiRequest request = new RegisterServiceApiRequest(requestHeader, data);

        ResponseEntity<ApiResponse<RegisterServiceApiResponse>> response = client.registerKey(request);

        if (response.getBody() == null) {
            throw new RuntimeException("API response null");
        }

        return response.getBody().data().publicKey();
    }

    @Override
    public void removeServiceKey() {
        RequestHeader requestHeader = requestHeader();
        RemoveKeyApiRequest.RemoveKeyApiData data =
                new RemoveKeyApiRequest.RemoveKeyApiData(config.getServiceId());

        RemoveKeyApiRequest request = new RemoveKeyApiRequest(requestHeader, data);

        ResponseEntity<ApiResponse<RemoveKeyApiResponse>> response = client.removeKey(request);

        if (response.getBody() == null) {
            throw new RuntimeException("API response null");
        }

    }

    @Override
    public String rotateKey() {
        RequestHeader requestHeader = requestHeader();
        RotateKeyApiRequest.RotateKeyApiData data =
                new RotateKeyApiRequest.RotateKeyApiData(config.getServiceId());

        RotateKeyApiRequest request = new RotateKeyApiRequest(requestHeader, data);

        ResponseEntity<ApiResponse<RotateKeyResponse>> response = client.rotateKey(request);

        if (response.getBody() == null) {
            throw new RuntimeException("API response null");
        }

        return response.getBody().data().generatedKey();
    }
}
