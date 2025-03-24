package in.jaysan.controller.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.jaysan.dto.products.SubCategoryRequest;
import in.jaysan.dto.products.SubCategoryResponse;
import in.jaysan.entity.products.Category;
import in.jaysan.entity.products.SubCategory;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.products.CategoryRepository;
import in.jaysan.repository.products.SubCategoryRepository;
import in.jaysan.service.products.SubCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin("*")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategoryController(SubCategoryService subCategoryService, SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        this.subCategoryService = subCategoryService;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubCategoryResponse> addSubCategory(
            @RequestPart("request") String requestJson,
            @RequestPart(value = "imageFile0", required = false) MultipartFile imageFile0,
            @RequestPart(value = "imageFile1", required = false) MultipartFile imageFile1,
            @RequestPart(value = "imageFile2", required = false) MultipartFile imageFile2,
            @RequestPart(value = "imageFile3", required = false) MultipartFile imageFile3,
            @RequestPart(value = "imageFile4", required = false) MultipartFile imageFile4) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SubCategoryRequest request = objectMapper.readValue(requestJson, SubCategoryRequest.class);

            List<MultipartFile> imageFiles = Arrays.asList(imageFile0, imageFile1, imageFile2, imageFile3, imageFile4);
            imageFiles.removeIf(Objects::isNull);

            return ResponseEntity.ok(subCategoryService.addSubCategory(request, imageFiles));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    // ✅ Get All SubCategories
    @GetMapping
    public List<SubCategoryResponse> getAllSubCategories() {
        return subCategoryService.getAllSubCategories();
    }

    // ✅ Get SubCategory by ID
    @GetMapping("/{id}")
    public SubCategoryResponse getSubCategoryById(@PathVariable Long id) {
        return subCategoryService.getSubCategoryById(id);
    }

    // ✅ Update SubCategory (with image)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubCategoryResponse updateSubCategory(
            @PathVariable Long id,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "imageFile0", required = false) MultipartFile imageFile0,
            @RequestPart(value = "imageFile1", required = false) MultipartFile imageFile1,
            @RequestPart(value = "imageFile2", required = false) MultipartFile imageFile2,
            @RequestPart(value = "imageFile3", required = false) MultipartFile imageFile3,
            @RequestPart(value = "imageFile4", required = false) MultipartFile imageFile4) {

        ObjectMapper objectMapper = new ObjectMapper();
        SubCategoryRequest request;

        try {
            request = objectMapper.readValue(requestJson, SubCategoryRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format", e);
        }

        List<MultipartFile> imageFiles = Arrays.asList(imageFile0, imageFile1, imageFile2, imageFile3, imageFile4);
        return subCategoryService.updateSubCategory(id, request, imageFiles);
    }

    // Updated Service Method




    // ✅ Delete SubCategory
    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
    }
}
