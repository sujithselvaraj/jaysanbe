package in.jaysan.dto.dealer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealerDto {

    private String dealerName;
    private String dealerPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String dealerLocation;
    private String dealerState;
    private String dealerEmail;


}
