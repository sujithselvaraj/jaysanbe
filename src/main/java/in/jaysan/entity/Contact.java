package in.jaysan.entity;

import in.jaysan.option.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_table")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String purpose;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String location;
    private String companyName;
    private String product;
    private String issue;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    private String comments;
}
