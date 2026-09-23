package aji.intern.restapi.client.dto;

public record ErrorResponse(
        ResponseHeader header,
        ErrorDetails error
) {
    public record ErrorDetails(
            String responseCode,
            String errorOrigin,
            String message
    ) {}
}
