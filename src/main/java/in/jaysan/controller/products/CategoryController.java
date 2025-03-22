package in.jaysan.controller.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.jaysan.dto.products.CategoryRequest;
import in.jaysan.dto.products.CategoryResponse;
import in.jaysan.service.products.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponse addCategory(
            @RequestPart("categoryRequest") String categoryRequestJson,
            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest categoryRequest = objectMapper.readValue(categoryRequestJson, CategoryRequest.class);

        // Handle category and image file logic here
        return categoryService.addCategory(categoryRequest, imageFile);
    }
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }





    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @RequestPart("categoryRequest") String categoryRequestJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest categoryRequest = objectMapper.readValue(categoryRequestJson, CategoryRequest.class);

        return categoryService.updateCategory(id, categoryRequest, imageFile);
    }


    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
