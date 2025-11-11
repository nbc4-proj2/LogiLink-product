package product_service.product.domain.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import product_service.product.domain.model.entity.Product;
import product_service.product.domain.model.entity.ProductStatus;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByProductName(String productName);
    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);
}
