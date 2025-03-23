package in.jaysan.service;

import in.jaysan.dto.dealer.DealerDto;
import in.jaysan.dto.dealer.DealerResponseDTO;
import in.jaysan.entity.Dealer;
import in.jaysan.repository.DealerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DealerService {

    private DealerRepository dealerRepository;

    public Dealer save(Dealer dealer) {
        dealerRepository.save(dealer);
        return dealer;
    }

    public Dealer update(DealerDto dealer, Long id) {
        Dealer existingDealer = dealerRepository.getById(id);
        existingDealer.setDealerName(dealer.getDealerName());
        existingDealer.setDealerPhoneNumber(dealer.getDealerPhoneNumber());
        existingDealer.setAddressLine1(dealer.getAddressLine1());
        existingDealer.setAddressLine2(dealer.getAddressLine2());
        existingDealer.setDealerLocation(dealer.getDealerLocation());
        existingDealer.setDealerState(dealer.getDealerState());
        existingDealer.setDealerEmail(dealer.getDealerEmail());
        return dealerRepository.save(existingDealer);
    }

    public void delete(Long id) {
        dealerRepository.deleteById(id);
    }

    public List<Dealer> getAll() {
        return dealerRepository.findAll();
    }

    public List<Dealer> getByState(String dealerState) {
        return dealerRepository.findByDealerState(dealerState);
    }


    public DealerResponseDTO getDealerById(Long id) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found with ID: " + id));

        return new DealerResponseDTO(
                dealer.getId(),
                dealer.getDealerName(),
                dealer.getDealerPhoneNumber(),
                dealer.getDealerEmail(),
                dealer.getAddressLine1(),
                dealer.getAddressLine2(),
                dealer.getDealerLocation(),
                dealer.getDealerState()
        );
    }

}
