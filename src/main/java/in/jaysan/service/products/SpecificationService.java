package in.jaysan.service.products;

import in.jaysan.dto.products.SpecificationRequest;
import in.jaysan.dto.products.SpecificationResponse;
import in.jaysan.entity.products.Specification;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.products.SpecificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpecificationService {
    private final SpecificationRepository specificationRepository;

    public SpecificationService(SpecificationRepository specificationRepository) {
        this.specificationRepository = specificationRepository;
    }

    // ✅ Add Specification
    public SpecificationResponse addSpecification(SpecificationRequest request) {
        Specification specification = new Specification(null, request.getCategory(),
                request.getSubCategory(), request.getYoutubeLink(), request.getSpecificationDetails());

        Specification savedSpecification = specificationRepository.save(specification);
        return new SpecificationResponse(savedSpecification.getId(), savedSpecification.getCategory(),
                savedSpecification.getSubCategory(), savedSpecification.getYoutubeLink(), savedSpecification.getSpecificationDetails());
    }

    // ✅ Get All Specifications
    public List<SpecificationResponse> getAllSpecifications() {
        List<Specification> specifications = specificationRepository.findAll();
        if (specifications.isEmpty()) {
            throw new ResourceNotFoundException("No specifications found");
        }
        return specifications.stream()
                .map(s -> new SpecificationResponse(s.getId(), s.getCategory(), s.getSubCategory(),
                        s.getYoutubeLink(), s.getSpecificationDetails()))
                .collect(Collectors.toList());
    }
    public SpecificationResponse getSpecificationById(Long id) {
        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification not found with id: " + id));

        return new SpecificationResponse(
                specification.getId(),
                specification.getCategory(),
                specification.getSubCategory(),
                specification.getYoutubeLink(),
                specification.getSpecificationDetails() //  Return existing map directly
        );
    }

}