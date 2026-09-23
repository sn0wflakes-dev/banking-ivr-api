package aji.intern.restapi.error;

import aji.intern.restapi.dto.WebResponse;
import aji.intern.restapi.filter.MessageHeaderVal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> validationEx(
            ConstraintViolationException ex,
            HttpServletRequest httpServletRequest) {
        String messageId = (String) httpServletRequest.getAttribute(MessageHeaderVal.MSG_ID.toString());
        List<Map<String, String>> violationList = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    Map<String, String> errorList = new HashMap<>();
                    errorList.put("Field", constraintViolation.getPropertyPath().toString());
                    errorList.put("Message", constraintViolation.getMessage());
                    return errorList;
                }).toList();

        /*
         * Put validation list to logger using mdc (ThreadContext Log4J)
         * */
        try {
            ThreadContext.put("violationList", objectMapper.writeValueAsString(violationList));
            log.warn("Failed to make request. Reason : Invalid request");
        } finally {
            ThreadContext.remove("violationList");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .header(responseHeader(messageId))
                        .error(WebResponse.ErrorMessage.builder()
                                .responseCode("99")
                                .errorOrigin("IVR-API")
                                .message("Validation Error")
                                .build())
                        .build());
    }

    @ExceptionHandler(SoapClientException.class)
    public ResponseEntity<WebResponse<String>> handleSoapClientException(
            SoapClientException ex,
            HttpServletRequest httpServletRequest) {

        String messageId = (String) httpServletRequest.getAttribute(MessageHeaderVal.MSG_ID.toString());
        log.warn("Failed executing request. Reason : {}", ex.getResponseMessage());

        switch (ex.getResponseCode()) {
            case "04":
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(WebResponse.<String>builder()
                                .header(responseHeader(messageId))
                                .error(WebResponse.ErrorMessage.builder()
                                        .responseCode(ex.getResponseCode())
                                        .errorOrigin(ex.getErrorOrigin())
                                        .message(ex.getResponseMessage())
                                        .build())
                                .build());
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(WebResponse.<String>builder()
                                .header(responseHeader(messageId))
                                .error(WebResponse.ErrorMessage.builder()
                                        .responseCode(ex.getResponseCode())
                                        .errorOrigin(ex.getErrorOrigin())
                                        .message("Unhandled exception")
                                        .build())
                                .build());
        }

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> handleGenericException(
            Exception ex,
            HttpServletRequest httpServletRequest) {
        String messageId = (String) httpServletRequest.getAttribute(MessageHeaderVal.MSG_ID.toString());
        log.error("Failed to make request. Reason : Internal server error, details : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder()
                        .header(responseHeader(messageId))
                        .error(WebResponse.ErrorMessage.builder()
                                .responseCode("99")
                                .errorOrigin("IVR-API")
                                .message("500 Internal Server Error")
                                .build())
                        .build());
    }

    private WebResponse.ResponseHeader responseHeader(String messageId) {
        return WebResponse.ResponseHeader.builder()
                .requestId(messageId)
                .timestamp(OffsetDateTime.now().toString())
                .build();
    }

}
