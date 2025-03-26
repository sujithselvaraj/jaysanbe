package in.jaysan.controller;

import in.jaysan.entity.Brochure;
import in.jaysan.repository.BrochureRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;
@RestController
@RequestMapping("/api/brochure")
public class BrochureController {

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    private final S3Client s3Client;
    private final BrochureRepository brochureRepository;

    public BrochureController(BrochureRepository brochureRepository, S3Client s3Client) {
        this.brochureRepository = brochureRepository;
        this.s3Client = s3Client;
    }

    @PostMapping()
    public String uploadBrouchure(@RequestPart(value = "brochure") MultipartFile brouchure) throws IOException {
        String fileNameExtension = brouchure.getOriginalFilename()
                .substring(brouchure.getOriginalFilename().lastIndexOf(".") + 1);

        String key = UUID.randomUUID().toString() + "." + fileNameExtension;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl("public-read")
                .contentType(brouchure.getContentType())
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(brouchure.getBytes()));

        if (response.sdkHttpResponse().isSuccessful()) {
            String brochureUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

            // Save URL in Database
            Brochure brochure = new Brochure();
            brochure.setBrochureUrl(brochureUrl);
            brochureRepository.save(brochure);

            return brochureUrl;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File Upload failed");
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<String> getLatestBrochure() {
        return brochureRepository.findTopByOrderByIdDesc()
                .map(brochure -> ResponseEntity.ok(brochure.getBrochureUrl()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No brochure found"));
    }
}
