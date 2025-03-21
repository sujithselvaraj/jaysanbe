package in.jaysan.service.products;

import in.jaysan.dto.products.ProductRequest;
import in.jaysan.dto.products.ProductResponse;
import in.jaysan.entity.products.Product;
import in.jaysan.exception.ResourceNotFoundException;
import in.jaysan.repository.products.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = "uploads/";

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    //  Add Product
    public ProductResponse addProduct(ProductRequest request) {
        String imageUrl = saveImage(request.getImageUrl());

        Product product = Product.builder()
                .category(request.getCategory())
                .subCategory(request.getSubCategory())
                .description(request.getDescription())
                .features(request.getFeatures())
                .imageUrl(imageUrl)
                .build();

        productRepository.save(product);

        return ProductResponse.builder()
                .id(product.getId())
                .category(product.getCategory())
                .subCategory(product.getSubCategory())
                .description(product.getDescription())
                .features(product.getFeatures())
                .imageUrl(product.getImageUrl())
                .build();
    }


    // Get All Products
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getImageUrl(), p.getCategory(), p.getSubCategory(), p.getDescription(), p.getFeatures()))
                .collect(Collectors.toList());
    }


    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return new ProductResponse(
                product.getId(),
                product.getCategory(),
                product.getSubCategory(),
                product.getDescription(),
                product.getImageUrl(),
                product.getFeatures()
        );
    }

    private String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + filename;

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // Create the directory if it doesn't exist
            }

            file.transferTo(Paths.get(filePath)); // Save file to uploads folder
            return filePath; // Store relative path in DB
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}