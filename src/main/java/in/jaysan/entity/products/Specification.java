package in.jaysan.entity.products;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String subCategory;
    private String youtubeLink;

    @ElementCollection
    @CollectionTable(name = "specifications", joinColumns = @JoinColumn(name = "specification_id"))
    @MapKeyColumn(name = "specification_label")
    @Column(name = "specification_value")
    private Map<String, String> specificationDetails;
}