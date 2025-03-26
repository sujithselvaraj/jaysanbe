package in.jaysan.controller;

import in.jaysan.dto.testimonial.TestimonialRequest;
import in.jaysan.dto.testimonial.TestimonialResponse;
import in.jaysan.service.TestimonialService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/testimonial")
@AllArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<TestimonialResponse> addTestimonial(
            @RequestPart("testimonialRequest")TestimonialRequest testimonialRequest,
            @RequestPart(value = "imageFile")MultipartFile imageFile) throws IOException {
        System.out.println("Received TestimonialRequest: " + testimonialRequest.getProductName());

        return ResponseEntity.ok(testimonialService.addTestimonial(testimonialRequest,imageFile));
    }

    @GetMapping
    public ResponseEntity<List<TestimonialResponse>> getAllTestimonial() {
        return ResponseEntity.ok(testimonialService.getAllTestimonials());
    }
}
