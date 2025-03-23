package in.jaysan.controller.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.jaysan.dto.products.SubCategoryRequest;
import in.jaysan.dto.products.SubCategoryResponse;
import in.jaysan.service.products.SubCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin("*")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    // ✅ Add SubCategory (with image)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubCategoryResponse> addSubCategory(
            @RequestPart("request") String requestJson,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("imageFile1") MultipartFile imageFile1,
            @RequestPart("imageFile2") MultipartFile imageFile2,
             @RequestPart("imageFile3") MultipartFile imageFile3,
                    @RequestPart("imageFile4") MultipartFile imageFile4
            ) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SubCategoryRequest request = objectMapper.readValue(requestJson, SubCategoryRequest.class);
            return ResponseEntity.ok(subCategoryService.addSubCategory(request, imageFile,imageFile1,imageFile2,imageFile3,imageFile4));
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
            @RequestPart("request") String requestJson,  // Receive as JSON String
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "imageFile1", required = false) MultipartFile imageFile1,
            @RequestPart(value = "imageFile2", required = false) MultipartFile imageFile2,
            @RequestPart(value = "imageFile3", required = false) MultipartFile imageFile3,
            @RequestPart(value = "imageFile4", required = false) MultipartFile imageFile4) {

        // Convert JSON string to Java Object
        ObjectMapper objectMapper = new ObjectMapper();
        SubCategoryRequest request;
        try {
            request = objectMapper.readValue(requestJson, SubCategoryRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format", e);
        }

        // Set image files
        request.setImageFile(imageFile);
        request.setImageFile1(imageFile1);
        request.setImageFile2(imageFile2);
        request.setImageFile3(imageFile3);
        request.setImageFile4(imageFile4);

        return subCategoryService.updateSubCategory(id, request);
    }


    // ✅ Delete SubCategory
    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
    }
}
