package in.jaysan.controller.products;

import in.jaysan.dto.products.ProductRequest;
import in.jaysan.dto.products.ProductResponse;
import in.jaysan.entity.products.Product;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.service.products.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam("name") String name,
            @RequestParam("subCategory") String subCategory,
            @RequestParam("description") String description,
            @RequestParam("features") List<String> features,
            @RequestParam("image") MultipartFile image) {

        ProductRequest request = new ProductRequest();
        request.setCategory(name);
        request.setSubCategory(subCategory);
        request.setDescription(description);
        request.setFeatures(features);
        request.setImageUrl(image);

        return ResponseEntity.ok(productService.addProduct(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
