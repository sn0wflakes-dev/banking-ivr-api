package aji.intern.restapi.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateCardResponse {
    private String customerNumber;
    private String name;
    private String address;
    private String cif;
    private String cardNumber;
}
