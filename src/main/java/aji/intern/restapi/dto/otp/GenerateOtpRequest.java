package aji.intern.restapi.dto.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateOtpRequest {
    @NotBlank(message = "Customer Identification Folder field is required")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Customer Identification Folder must contains only letters and number")
    private String cif;
}
