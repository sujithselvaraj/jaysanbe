package in.jaysan.controller.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.jaysan.dto.products.SubCategoryRequest;
import in.jaysan.dto.products.SubCategoryResponse;
import in.jaysan.service.products.SubCategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart("imageFile") MultipartFile imageFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SubCategoryRequest request = objectMapper.readValue(requestJson, SubCategoryRequest.class);
            return ResponseEntity.ok(subCategoryService.addSubCategory(request, imageFile));
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
    @PutMapping("/{id}")
    public SubCategoryResponse updateSubCategory(@PathVariable Long id,
                                                 @RequestPart("request") SubCategoryRequest request,
                                                 @RequestPart("imageFile") MultipartFile imageFile) {
        request.setImageFile(imageFile);
        return subCategoryService.updateSubCategory(id, request);
    }

    // ✅ Delete SubCategory
    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
    }
}
