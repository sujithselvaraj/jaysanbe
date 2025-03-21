package in.jaysan.repository;

import in.jaysan.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findByPurposeIgnoreCase(String purpose);

}
