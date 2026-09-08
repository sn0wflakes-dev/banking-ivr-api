package aji.intern.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoapFaultResponse {
    private String responseCode;
    private String messageId;
    private String errorOrigin;
    private String responseMessage;
}
