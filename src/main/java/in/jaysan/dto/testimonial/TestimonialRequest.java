package in.jaysan.dto.testimonial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestimonialRequest {
    private String customerName;
    private String customerLocation;
    private String productName;
    private String customerReview;
}
