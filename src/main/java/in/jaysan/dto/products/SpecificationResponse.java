package in.jaysan.dto.products;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationResponse {
    private Long id;
    private String category;
    private String subCategory;
    private String youtubeLink;
    private Map<String, String> specificationDetails;
}