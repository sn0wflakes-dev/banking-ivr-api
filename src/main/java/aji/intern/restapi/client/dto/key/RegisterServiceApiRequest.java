package aji.intern.restapi.client.dto.key;

import aji.intern.restapi.client.dto.RequestHeader;

public record RegisterServiceApiRequest(
        RequestHeader requestHeader,
        RegisterServiceApiData registerServiceData
) {
    public record RegisterServiceApiData(
            String serviceId
    ) {}
}
