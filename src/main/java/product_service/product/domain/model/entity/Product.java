package product_service.product.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import product_service.product.common.BaseTimeEntity;
import product_service.product.common.exception.AppException;
import product_service.product.common.exception.ProductErrorCode;

import java.util.UUID;

@Entity
@Table(name="p_products")
@Getter
@NoArgsConstructor(force = true)
@SuperBuilder
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

    //임의값
    @Column(nullable = false)
    private Long prdId;

    // 상품 수정 생성자
    public void update(
            String productName,
            String productDescription,
            Long productPrice,
            Long productQuantity) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

//    // 재고 증가
//    public void increaseProductQuantity(Long amount) {
//        if(amount < 0){
//            throw new AppException(ProductErrorCode.INVALID_QUANTITY_INCREASE);
//        }
//        this.productQuantity += amount;
//    }

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
    public void delete() {
        this.status = ProductStatus.INACTIVE;
    }

}
