package in.jaysan.repository.products;

import in.jaysan.entity.products.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}