package in.jaysan.dto.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String eventName;
    private String imageUrl;
    private String eventDesc;
}
