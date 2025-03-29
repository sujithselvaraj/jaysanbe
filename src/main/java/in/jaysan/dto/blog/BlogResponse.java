package in.jaysan.dto.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private String blogTitle;
    private String blogContent;
    private String blogImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
