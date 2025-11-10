package product_service.product.domain.model.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE("정상"),
    INACTIVE("비활성화");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }
}
