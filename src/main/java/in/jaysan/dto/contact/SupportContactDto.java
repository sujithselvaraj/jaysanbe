package in.jaysan.dto.contact;

import in.jaysan.option.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportContactDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String location;
    private String product;
    private String issue;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String comments;
}
