package in.jaysan.dto.products;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String imageUrl;
    private String category;
    private String subCategory;
    private String description;
    private List<String> features;
}