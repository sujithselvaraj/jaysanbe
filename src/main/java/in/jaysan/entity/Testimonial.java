package in.jaysan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "testimonial")
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String customerLocation;
    @Column(nullable = false)
    private String productName;
    @Lob
    @Column(nullable = false)
    private String customerReview;
    private String imageUrl;
}
