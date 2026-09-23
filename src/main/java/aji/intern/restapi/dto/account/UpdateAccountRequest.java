package aji.intern.restapi.dto.account;

import jakarta.validation.constraints.Email;
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
public class UpdateAccountRequest {
    @NotBlank(message = "Customer Identification Folder field is required")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Customer Identification Folder must contains only letters and number")
    private String cif;

    @Email(message = "Invalid email format")
    private String emailAddress;

    @Pattern(regexp = "^[0-9]{1,20}$", message = "Phone number must contain only numbers and be up to 20 digits long")
    private String phoneNumber;
}
