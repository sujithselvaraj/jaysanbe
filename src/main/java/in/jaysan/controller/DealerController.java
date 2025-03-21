package in.jaysan.controller;

import in.jaysan.dto.dealer.DealerDto;
import in.jaysan.entity.Dealer;
import in.jaysan.service.DealerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/dealer")
public class DealerController {

    private DealerService dealerService;

    @PostMapping("/save")
    public ResponseEntity<Dealer> saveDealer(@RequestBody Dealer dealer) {
        dealerService.save(dealer);
        return ResponseEntity.ok(dealer);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateDealer(@PathVariable("id") Long id , @RequestBody DealerDto dealer) {
        dealerService.update(dealer,id);
        return ResponseEntity.ok("Dealer Updated Successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDealer(@PathVariable Long id) {
        dealerService.delete(id);
        return ResponseEntity.ok("Dealer Deleted Successfully");
    }

    @GetMapping()
    public ResponseEntity<List<Dealer>> getAllDealers()
    {
        return ResponseEntity.ok(dealerService.getAll());
    }

    @GetMapping("/{dealerState}")
    public ResponseEntity<List<Dealer>> getDealersByState(@PathVariable String dealerState) {
        return ResponseEntity.ok(dealerService.getByState(dealerState));
    }

}
