package in.jaysan.dto.products;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String categoryName;
    private String imageUrl;
    private String description;
    private List<SubCategoryResponse> subCategories;
}
