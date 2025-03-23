package in.jaysan.dto.career;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String message;
    private String resumeUrl;
}
