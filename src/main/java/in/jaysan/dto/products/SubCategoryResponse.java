package in.jaysan.dto.products;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponse {
    private Long id;
    private String subCategoryName;
    private String categoryName;
    private List<String> features;
    private String youtubeLink;
    private Map<String, String> specificationDetails;
    private String imagePath;

    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String imagePath4;
    private String brochure;
}
