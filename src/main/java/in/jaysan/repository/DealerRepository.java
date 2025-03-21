package in.jaysan.repository;

import in.jaysan.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealerRepository extends JpaRepository<Dealer,Long> {
    List<Dealer> findByDealerState(String dealerState);

}
