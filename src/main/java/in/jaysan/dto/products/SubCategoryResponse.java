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
    private String categoryName;  // Include category name for reference
    private List<String> features;
    private String youtubeLink;
    private Map<String, String> specificationDetails;
    private String imagePath;  // 🆕 Added to return the image URL
}
