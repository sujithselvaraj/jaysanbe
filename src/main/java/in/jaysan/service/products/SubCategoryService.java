package in.jaysan.service.products;

import in.jaysan.dto.products.SubCategoryRequest;
import in.jaysan.dto.products.SubCategoryResponse;
import in.jaysan.entity.products.Category;
import in.jaysan.entity.products.SubCategory;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.products.CategoryRepository;
import in.jaysan.repository.products.SubCategoryRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;


    private final S3Client s3Client;

    private final String UPLOAD_DIR = "uploads/subcategories/";  // 🆕 Directory for image storage

    public SubCategoryService(SubCategoryRepository subCategoryRepository,
                              CategoryRepository categoryRepository,
                              S3Client s3Client
                              ) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.s3Client=s3Client;
    }

    // ✅ Add SubCategory
    public SubCategoryResponse addSubCategory(SubCategoryRequest request, List<MultipartFile> imageFiles) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(request.getSubCategoryName());
        subCategory.setCategory(category);
        subCategory.setFeatures(request.getFeatures());
        subCategory.setYoutubeLink(request.getYoutubeLink());
        subCategory.setSpecificationDetails(request.getSpecificationDetails());

        // Handle image upload
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                imagePaths.add(saveImage(imageFile));
            }
        }

        // Assign images to their respective fields
        if (imagePaths.size() > 0) subCategory.setImagePath(imagePaths.get(0));
        if (imagePaths.size() > 1) subCategory.setImagePath1(imagePaths.get(1));
        if (imagePaths.size() > 2) subCategory.setImagePath2(imagePaths.get(2));
        if (imagePaths.size() > 3) subCategory.setImagePath3(imagePaths.get(3));
        if (imagePaths.size() > 4) subCategory.setImagePath4(imagePaths.get(4));

        SubCategory saved = subCategoryRepository.save(subCategory);
        return mapToResponse(saved);
    }


    //  Get All SubCategories
    public List<SubCategoryResponse> getAllSubCategories() {
        return subCategoryRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    //  Get SubCategory by ID
    public SubCategoryResponse getSubCategoryById(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        return mapToResponse(subCategory);
    }

    //  Update SubCategory
    public SubCategoryResponse updateSubCategory(Long id, SubCategoryRequest request, List<MultipartFile> imageFiles) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            subCategory.setCategory(category);
        }

        subCategory.setSubCategoryName(request.getSubCategoryName());
        subCategory.setFeatures(request.getFeatures());
        subCategory.setYoutubeLink(request.getYoutubeLink());
        subCategory.setSpecificationDetails(request.getSpecificationDetails());

        // Handle image update
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                imagePaths.add(saveImage(imageFile));
            }
        }

        if (imagePaths.size() > 0) subCategory.setImagePath(imagePaths.get(0));
        if (imagePaths.size() > 1) subCategory.setImagePath1(imagePaths.get(1));
        if (imagePaths.size() > 2) subCategory.setImagePath2(imagePaths.get(2));
        if (imagePaths.size() > 3) subCategory.setImagePath3(imagePaths.get(3));
        if (imagePaths.size() > 4) subCategory.setImagePath4(imagePaths.get(4));

        return mapToResponse(subCategoryRepository.save(subCategory));
    }

    // ✅ Delete SubCategory
    public void deleteSubCategory(Long id) {
        subCategoryRepository.deleteById(id);
    }

    // 🔄 Helper Method to save image
    public String saveImage(MultipartFile file) {

        String fileNameExtension= file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf(".")+1);

        // generate the unique id for the string
        String key=UUID.randomUUID().toString()+"."+fileNameExtension;
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


    // 🔄 Helper Method to map SubCategory to Response DTO
    public SubCategoryResponse mapToResponse(SubCategory subCategory) {
        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getSubCategoryName(),
                subCategory.getCategory().getCategoryName(),
                subCategory.getFeatures(),
                subCategory.getYoutubeLink(),
                subCategory.getSpecificationDetails(),
                subCategory.getImagePath(),
                subCategory.getImagePath1(),
                subCategory.getImagePath2(),
                subCategory.getImagePath3(),
                subCategory.getImagePath4()
        );
    }
}
