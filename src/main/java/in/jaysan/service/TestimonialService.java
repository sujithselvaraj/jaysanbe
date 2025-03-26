package in.jaysan.service;

import in.jaysan.dto.testimonial.TestimonialRequest;
import in.jaysan.dto.testimonial.TestimonialResponse;
import in.jaysan.entity.Testimonial;
import in.jaysan.repository.TestimonialRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;
    private final S3Client s3Client;

    public TestimonialService(TestimonialRepository testimonialRepository, S3Client s3Client) {
        this.testimonialRepository = testimonialRepository;
        this.s3Client = s3Client;
    }
    public TestimonialResponse addTestimonial(TestimonialRequest testimonialRequest, MultipartFile image) throws IOException {
        String filePath = image != null ? saveImage(image) : null;
        Testimonial testimonial = new Testimonial();

        testimonial.setCustomerLocation(testimonialRequest.getCustomerLocation());
        testimonial.setCustomerName(testimonialRequest.getCustomerName());
        testimonial.setImageUrl(filePath);
        testimonial.setCustomerReview(testimonialRequest.getCustomerReview());
        testimonial.setProductName(testimonialRequest.getProductName());

        testimonial = testimonialRepository.save(testimonial);
        return mapToTestimonialResponse(testimonial);
    }

    public List<TestimonialResponse> getAllTestimonials() {
       return testimonialRepository.findAll().stream().map(this ::mapToResponse).toList();
    }

    private TestimonialResponse mapToResponse(Testimonial testimonial) {
        return new TestimonialResponse(
                testimonial.getId(),
                testimonial.getCustomerName(),
                testimonial.getCustomerLocation(),
                testimonial.getProductName(),
                testimonial.getCustomerReview(),
                testimonial.getImageUrl()
        );
    }

    private TestimonialResponse mapToTestimonialResponse(Testimonial testimonial) {
        String imageUrl = testimonial.getImageUrl() != null ? "/images/" + testimonial.getImageUrl() : null;
        return new TestimonialResponse(testimonial.getId(), testimonial.getCustomerName(), imageUrl, testimonial.getProductName(), testimonial.getCustomerReview(), testimonial.getCustomerLocation());
    }

    private String saveImage(MultipartFile file) {

        String fileNameExtension= file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf(".")+1);

        String key= UUID.randomUUID().toString()+"."+fileNameExtension;
        try {
            PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            // store in bucket
            PutObjectResponse response= s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful())
            {
                return "https://"+bucketName+".s3.amazonaws.com/"+key;

            }
            else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File Upload failed");
            }
        }

        catch(IOException e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error Occured while uplaodung the file");
        }
    }

}
