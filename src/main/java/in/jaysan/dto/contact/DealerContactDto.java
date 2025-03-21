package in.jaysan.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealerContactDto {

    private String name;
    private String phoneNumber;
    private String location;
    private String product;
    private String companyName;
}
