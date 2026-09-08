package aji.intern.restapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebResponse<T> {
    private ResponseHeader header;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorMessage error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseHeader {
        private String requestId;
        private String timestamp;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorMessage {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String responseCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String errorOrigin;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object detail;
    }
}
