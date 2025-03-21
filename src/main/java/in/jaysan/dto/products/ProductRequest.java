package in.jaysan.dto.products;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
public class ProductRequest {
    private MultipartFile imageUrl;
    private String category;
    private String subCategory;
    private String description;
    private List<String> features;
}