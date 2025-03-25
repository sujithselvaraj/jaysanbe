package in.jaysan.dto.contact;

import in.jaysan.option.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealerContactDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String location;
    private String product;
    private String companyName;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String comments;
}
