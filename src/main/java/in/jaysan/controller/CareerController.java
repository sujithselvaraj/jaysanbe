package in.jaysan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.jaysan.dto.career.CareerRequestDTO;
import in.jaysan.dto.career.CareerResponseDTO;
import in.jaysan.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CareerController {
    private final CareerService careerService;

    // Apply for Career
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CareerResponseDTO> applyForCareer(
            @RequestPart("career") String careerJson,
            @RequestPart("resume") MultipartFile resume) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CareerRequestDTO careerRequest = objectMapper.readValue(careerJson, CareerRequestDTO.class);
            CareerResponseDTO response = careerService.applyForCareer(careerRequest, resume);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
    }

    // Get All Careers
    @GetMapping
    public ResponseEntity<List<CareerResponseDTO>> getAllCareers() {
        return ResponseEntity.ok(careerService.getAllCareers());
    }

    // Get Career by ID
    @GetMapping("/{id}")
    public ResponseEntity<CareerResponseDTO> getCareerById(@PathVariable Long id) {
        return ResponseEntity.ok(careerService.getCareerById(id));
    }

    // Update Career
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CareerResponseDTO> updateCareer(
            @PathVariable Long id,
            @RequestPart("career") String careerJson,
            @RequestPart(value = "resume", required = false) MultipartFile resume) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CareerRequestDTO careerRequest = objectMapper.readValue(careerJson, CareerRequestDTO.class);

            CareerResponseDTO updatedCareer = careerService.updateCareer(id, careerRequest, resume);
            return ResponseEntity.ok(updatedCareer);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
    }



    // Delete Career
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCareer(@PathVariable Long id) {
        careerService.deleteCareer(id);
        return ResponseEntity.ok("Career entry deleted successfully");
    }
}
