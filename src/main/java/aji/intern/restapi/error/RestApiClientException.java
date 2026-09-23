package aji.intern.restapi.error;

import aji.intern.restapi.client.dto.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class RestApiClientException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final ErrorResponse errorResponse;

    public RestApiClientException(HttpStatusCode statusCode, ErrorResponse errorResponse) {
        super(errorResponse != null && errorResponse.error() != null ? errorResponse.error().message() : "API Error");
        this.statusCode = statusCode;
        this.errorResponse = errorResponse;
    }
}
