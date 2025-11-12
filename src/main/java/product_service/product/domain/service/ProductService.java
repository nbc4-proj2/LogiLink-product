package product_service.product.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import product_service.product.domain.model.dto.request.ProductCreateReq;
import product_service.product.domain.model.dto.request.ProductUpdateReq;
import product_service.product.domain.model.dto.response.ProductRes;

import java.util.UUID;

public interface ProductService {

    ProductRes createProduct(ProductCreateReq requestDto, Long userId, String userRole, UUID hubId, UUID companyId);

    ProductRes updateProduct(UUID productId, ProductUpdateReq requestDto, Long userId, String userRole, UUID hubId, UUID companyId);

    void deleteProduct(UUID productId, Long userId, String userRole, UUID hubId, UUID companyId);

    ProductRes getProduct(UUID productId);

    ProductRes getPrdId(Long prdId);

    Page<ProductRes> getProductPage(Pageable pageable);

    void decreaseStock(UUID productId, Long amount);
}
