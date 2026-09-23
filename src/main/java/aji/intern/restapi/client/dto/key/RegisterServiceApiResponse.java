package aji.intern.restapi.client.dto.key;

public record RegisterServiceApiResponse(
        String serviceId,
        String publicKey
) {
}
