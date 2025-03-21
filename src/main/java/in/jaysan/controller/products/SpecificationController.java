package in.jaysan.controller.products;

import in.jaysan.dto.products.SpecificationRequest;
import in.jaysan.dto.products.SpecificationResponse;
import in.jaysan.entity.products.Specification;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.service.products.SpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {
    private final SpecificationService specificationService;

    public SpecificationController(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    @PostMapping("/add")
    public ResponseEntity<SpecificationResponse> addSpecification(@RequestBody SpecificationRequest request) {
        return ResponseEntity.ok(specificationService.addSpecification(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpecificationResponse>> getAllSpecifications() {
        List<SpecificationResponse> specifications = specificationService.getAllSpecifications();
        if (specifications.isEmpty()) {
            throw new ResourceNotFoundException("No specifications found");
        }
        return ResponseEntity.ok(specifications);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SpecificationResponse> getSpecificationById(@PathVariable Long id) {
        return ResponseEntity.ok(specificationService.getSpecificationById(id));
    }
}
