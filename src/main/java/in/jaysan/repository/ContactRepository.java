package in.jaysan.repository;

import in.jaysan.entity.Contact;
import in.jaysan.option.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findByPurposeIgnoreCase(String purpose);
    @Query("SELECT c FROM Contact c WHERE (:purpose IS NULL OR c.purpose ILIKE :purpose) AND (:status IS NULL OR c.status = :status)")
    List<Contact> findByPurposeAndStatus(@Param("purpose") String purpose, @Param("status") Status status);
}
