package aji.intern.restapi.client.dto.key;

import aji.intern.restapi.client.dto.RequestHeader;

public record RemoveKeyApiRequest(
        RequestHeader requestHeader,
        RemoveKeyApiData removeServiceData
) {
    public record RemoveKeyApiData(
            String serviceId
    ) {
    }
}
