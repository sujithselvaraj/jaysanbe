package in.jaysan.service.products;

import in.jaysan.dto.products.CategoryRequest;
import in.jaysan.dto.products.CategoryResponse;
import in.jaysan.dto.products.SubCategoryResponse;
import in.jaysan.entity.products.Category;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.products.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final String uploadDir = "uploads/";

    @Value("${aws.s3.bucketname}")
    private String bucketName;


    private final S3Client s3Client;



    public CategoryService(CategoryRepository categoryRepository,S3Client s3Client) {
        this.categoryRepository = categoryRepository;
        this.s3Client = s3Client;
    }

    // ✅ Get All Categories (With Subcategories)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ✅ Get Single Category by ID
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToResponse(category);
    }
    public CategoryResponse addCategory(CategoryRequest categoryRequest, MultipartFile imageFile) throws IOException {
        String filePath = saveImage(imageFile);
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setImageUrl(filePath);
        category.setDescription(categoryRequest.getDescription());
        category = categoryRepository.save(category);
        return mapToCategoryResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest, MultipartFile imageFile) throws IOException {
        return categoryRepository.findById(id).map(category -> {
            System.out.println("Updating category with ID: " + id);

            if (imageFile != null && !imageFile.isEmpty()) {
                System.out.println("Saving new image...");
                category.setImageUrl(saveImage(imageFile));
            }

            category.setCategoryName(categoryRequest.getCategoryName());
            category.setDescription(categoryRequest.getDescription());

            Category updatedCategory = categoryRepository.save(category);
            System.out.println("Category updated successfully!");

            return mapToCategoryResponse(updatedCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }


    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

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



    private CategoryResponse mapToCategoryResponse(Category category) {
        String imageUrl = category.getImageUrl() != null ? "/images/" + category.getImageUrl() : null;
        return new CategoryResponse(category.getId(), category.getCategoryName(), imageUrl, category.getDescription(), null);
    }

    // 🔄 Helper Method to Map Entity to Response
    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCategoryName(),
                category.getImageUrl(),
                category.getDescription(),
                category.getSubCategories() != null ? category.getSubCategories().stream()
                        .map(sub -> new SubCategoryResponse(
                                sub.getId(),
                                sub.getSubCategoryName(),
                                category.getCategoryName(),
                                sub.getFeatures(),
                                sub.getYoutubeLink(),
                                sub.getSpecificationDetails(),
                                sub.getImagePath(),
                                sub.getImagePath1(),
                                sub.getImagePath2(),
                                sub.getImagePath3(),
                                sub.getImagePath4()
                        ))
                        .collect(Collectors.toList()) : null  // Prevent NullPointerException
        );
    }
}