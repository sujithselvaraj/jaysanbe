package in.jaysan.dto.products;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequest {
    private String subCategoryName;
    private Long categoryId;  // Reference to Category
    private List<String> features;
    private String youtubeLink;
    private Map<String, String> specificationDetails;
    private MultipartFile imageFile;  //  Added for image upload

    private MultipartFile imageFile1;
    private MultipartFile imageFile2;
    private MultipartFile imageFile3;
    private MultipartFile imageFile4;
    private MultipartFile brochure;
}
