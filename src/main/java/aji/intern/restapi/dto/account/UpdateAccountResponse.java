package aji.intern.restapi.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedPhoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updateEmail;
}
