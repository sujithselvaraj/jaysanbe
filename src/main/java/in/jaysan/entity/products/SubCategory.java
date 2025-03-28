package in.jaysan.entity.products;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ElementCollection
    private List<String> features;

    private String youtubeLink;
    private String imagePath;

    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String imagePath4;
    private String brochure;

    @ElementCollection
    private Map<String, String> specificationDetails;
}
