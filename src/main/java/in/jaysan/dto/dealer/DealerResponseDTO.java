package in.jaysan.dto.dealer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealerResponseDTO {
    private Long id;
    private String dealerName;
    private String dealerPhoneNumber;
    private String dealerEmail;
    private String addressLine1;
    private String addressLine2;
    private String dealerLocation;
    private String dealerState;
}
