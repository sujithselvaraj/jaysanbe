package in.jaysan.entity.products;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String category;
    private String subCategory;
    private String description;

    @ElementCollection
    private List<String> features;
}