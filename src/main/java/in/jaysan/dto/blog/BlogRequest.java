package in.jaysan.dto.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequest {
    private String blogTitle;

    private String blogContent;

}
