package product_service.product.domain.model.entity;

import com.sparta.logilinkcommon.common.BaseTimeEntity;
import com.sparta.logilinkcommon.common.constants.ProductStatus;
import com.sparta.logilinkcommon.common.exception.AppException;
import jakarta.persistence.*;
import lombok.*;
import product_service.product.common.ProductErrorCode;

import java.util.UUID;

@Entity
@Table(name="p_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(nullable = false, length=100)
    private String productName;

    @Column(nullable = false, columnDefinition = "text")
    private String productDescription;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private Long productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    // 허브 서비스의 허브 ID
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID hubId;

    // 업체 서비스의 업체 ID
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID companyId;

    // 상품 수정 생성자
    public void update(
            String productName,
            String productDescription,
            Long productPrice,
            Long productQuantity,
            ProductStatus status,
            UUID hubId,
            UUID companyId) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.status = status;
        this.hubId = hubId;
        this.companyId = companyId;
    }

    // 재고 증가
    public void increaseProductQuantity(Long amount) {
        if(amount < 0){
            throw new AppException(ProductErrorCode.INVALID_QUANTITY_INCREASE);
        }
        this.productQuantity += amount;
    }

    // 재고 감소
    public void decreaseProductQuantity(Long amount) {
        if(amount < 0){
            throw new AppException(ProductErrorCode.INVALID_QUANTITY_DECREASE);
        }
        if(this.productQuantity - amount < 0 ){
            throw new AppException(ProductErrorCode.INSUFFICIENT_STOCK);
        }
        this.productQuantity -= amount;
    }

    // 상품 상태 확인
    public boolean isActive() {
        return this.status == ProductStatus.ACTIVE;
    }

    // 상품 비활성화
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

}
