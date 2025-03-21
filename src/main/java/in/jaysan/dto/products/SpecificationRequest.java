package in.jaysan.dto.products;

import lombok.Data;

import java.util.Map;

@Data
public class SpecificationRequest {
    private String category;
    private String subCategory;
    private String youtubeLink;
    private Map<String, String> specificationDetails;
}