package in.jaysan.dto.contact;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportContactDto {
    private String name;
    private String phoneNumber;
    private String location;
    private String product;
    private String issue;

}
