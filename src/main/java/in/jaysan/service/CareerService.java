package in.jaysan.service;

import in.jaysan.dto.career.CareerRequestDTO;
import in.jaysan.dto.career.CareerResponseDTO;
import in.jaysan.entity.Career;
import in.jaysan.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerService {
    private final CareerRepository careerRepository;

    private final S3Client s3Client;

    @Value("${aws.s3.bucketname}")
    private String bucketName;


    // Apply for Career
    public CareerResponseDTO applyForCareer(CareerRequestDTO requestDTO, MultipartFile resume) {
        String resumeUrl = uploadFile(resume); // Upload resume to S3

        Career career = Career.builder()
                .fullName(requestDTO.getFullName())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .message(requestDTO.getMessage())
                .resumeUrl(resumeUrl)
                .build();

        Career savedCareer = careerRepository.save(career);
        return mapToDTO(savedCareer);
    }

    // Get All Careers
    public List<CareerResponseDTO> getAllCareers() {
        return careerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get Career by ID
    public CareerResponseDTO getCareerById(Long id) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + id));
        return mapToDTO(career);
    }

    public CareerResponseDTO updateCareer(Long id, CareerRequestDTO requestDTO, MultipartFile resume) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + id));

        // Update career details
        career.setFullName(requestDTO.getFullName());
        career.setEmail(requestDTO.getEmail());
        career.setPhoneNumber(requestDTO.getPhoneNumber());
        career.setMessage(requestDTO.getMessage());

        // If a new resume is uploaded, replace the old one
        if (resume != null && !resume.isEmpty()) {
            String newResumeUrl = uploadFile(resume);
            career.setResumeUrl(newResumeUrl);
        }

        Career updatedCareer = careerRepository.save(career);
        return mapToDTO(updatedCareer);
    }


    // Delete Career
    public void deleteCareer(Long id) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found with ID: " + id));
        careerRepository.delete(career);
    }

    // Map Entity to DTO
    private CareerResponseDTO mapToDTO(Career career) {
        return CareerResponseDTO.builder()
                .id(career.getId())
                .fullName(career.getFullName())
                .email(career.getEmail())
                .phoneNumber(career.getPhoneNumber())
                .message(career.getMessage())
                .resumeUrl(career.getResumeUrl())
                .build();
    }


    public String uploadFile(MultipartFile file) {

        String fileNameExtension= file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf(".")+1);

        // generate the unique id for the string
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
