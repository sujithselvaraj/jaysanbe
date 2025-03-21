package in.jaysan.repository.products;


import in.jaysan.entity.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndSubCategory(String category, String subCategory);
}
