package in.jaysan.dto.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {
    private String name;
    private String designation;
    private String email;
    private String whatsappNumber;
}
