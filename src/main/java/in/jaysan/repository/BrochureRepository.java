package in.jaysan.repository;

import in.jaysan.entity.Brochure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrochureRepository extends JpaRepository<Brochure,Long> {
    Optional<Brochure> findTopByOrderByIdDesc();
}
