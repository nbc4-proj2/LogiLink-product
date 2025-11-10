package product_service.product.domain.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product_service.product.domain.model.entity.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByProductName(String productName);
}
