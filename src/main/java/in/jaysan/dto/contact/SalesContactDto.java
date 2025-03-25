package in.jaysan.dto.contact;

import in.jaysan.option.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesContactDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String location;
    private String product;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String comments;
}
