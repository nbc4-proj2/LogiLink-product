package product_service.product.domain.model.dto.response;

import com.sparta.logilinkcommon.common.constants.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import product_service.product.domain.model.entity.Product;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductRes {

    private UUID productId;
    private String name;
    private String description;
    private Long price;
    private Long quantity;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public static ProductRes from(Product product){
        return ProductRes.builder()
                .productId(product.getProductId())
                .name(product.getProductName())
                .description(product.getProductDescription())
                .price(product.getProductPrice())
                .quantity(product.getProductQuantity())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updateAt(product.getUpdatedAt())
                .build();
    }

}
