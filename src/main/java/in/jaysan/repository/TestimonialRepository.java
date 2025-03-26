package in.jaysan.repository;

import in.jaysan.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial,Long> {

}
