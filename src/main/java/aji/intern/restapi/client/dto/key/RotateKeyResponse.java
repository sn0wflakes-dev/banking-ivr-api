package aji.intern.restapi.client.dto.key;

public record RotateKeyResponse(
        String serviceId,
        String generatedKey
) {
}
