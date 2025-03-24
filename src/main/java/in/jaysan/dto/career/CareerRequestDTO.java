package in.jaysan.dto.career;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerRequestDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
}
