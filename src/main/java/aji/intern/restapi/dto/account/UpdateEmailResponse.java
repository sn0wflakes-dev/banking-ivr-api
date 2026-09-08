package aji.intern.restapi.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailResponse {
    private String responseCode;
    private String updateEmail;
    private String message;
}
