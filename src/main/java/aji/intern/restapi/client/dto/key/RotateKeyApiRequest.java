package aji.intern.restapi.client.dto.key;

import aji.intern.restapi.client.dto.RequestHeader;

public record RotateKeyApiRequest(
        RequestHeader requestHeader,
        RotateKeyApiData rotateKeyData

) {
    public record RotateKeyApiData(
            String serviceId
    ) {}
}
