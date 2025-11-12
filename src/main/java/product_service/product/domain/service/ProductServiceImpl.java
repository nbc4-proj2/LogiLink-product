package product_service.product.domain.service;

import product_service.product.common.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product_service.product.common.exception.ProductErrorCode;
import product_service.product.domain.model.dto.request.ProductCreateReq;
import product_service.product.domain.model.dto.request.ProductUpdateReq;
import product_service.product.domain.model.dto.response.ProductRes;
import product_service.product.domain.model.entity.Product;
import product_service.product.domain.model.entity.ProductStatus;
import product_service.product.domain.model.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    //=====상품 생성(MASTER, HUB, COMPANY)=====//
    @Override
    @Transactional
    public ProductRes createProduct(ProductCreateReq requestDto, Long userId, String userRole, UUID hubId, UUID companyId){

        // 상품 생성 권한 검증
        validateCreatePermission(userRole, hubId, companyId);

        // 상품 생성
        if(productRepository.existsByProductName(requestDto.getProductName())){
            throw new AppException(ProductErrorCode.PRODUCT_NAME_DUPLICATE);
        }
        Product product = Product.builder()
                .productName(requestDto.getProductName())
                .productDescription(requestDto.getProductDescription())
                .productPrice(requestDto.getProductPrice())
                .productQuantity(requestDto.getProductQuantity())
                .status(ProductStatus.ACTIVE)
                .hubId(hubId)
                .companyId(companyId)
                .createdBy(userId)
                .prdId(requestDto.getPrdId())
                .build();
        return ProductRes.from(productRepository.save(product));
    }

    //====상품 수정(MASTER, HUB, COMPANY)=====//
    @Override
    @Transactional
    public ProductRes updateProduct(UUID productId, ProductUpdateReq requestDto, Long userId, String userRole, UUID hubId, UUID companyId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 상품 수정 권한 검증
        validateUpdatePermission(product, userRole, hubId, companyId);

        // 상품 수정
        if(!product.getProductName().equals(requestDto.getProductName()) && productRepository.existsByProductName(requestDto.getProductName())){
            throw new AppException(ProductErrorCode.PRODUCT_NAME_DUPLICATE);
        }

        product.update(
                requestDto.getProductName(),
                requestDto.getProductDescription(),
                requestDto.getProductPrice(),
                requestDto.getProductQuantity()
        );
        return ProductRes.from(productRepository.save(product));
    }

    //====상품 삭제(MASTER, HUB)=====//
    @Override
    @Transactional
    public void deleteProduct(UUID productId, Long userId, String userRole, UUID hubId, UUID companyId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 상품 삭제 권한 검증
        validateDeletePermission(product, userRole, hubId);

        product.delete();
    }

    //====상품 단건 조회(ALL)=====//
    @Override
    @Transactional(readOnly = true)
    public ProductRes getProduct(UUID productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductRes.from(product);
    }

    // 상품 prdId값으로 조회
    @Override
    @Transactional
    public ProductRes getPrdId(Long prdId){
        Product product = productRepository.findByPrdId(prdId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return ProductRes.from(product);
    }


    //====상품 목록 조회(ALL)=====//
    @Override
    @Transactional(readOnly = true)
    public Page<ProductRes> getProductPage(Pageable pageable){

        Page<Product> productPage = productRepository.findAllByStatus(ProductStatus.ACTIVE, pageable);
        return productPage.map(ProductRes::from);
    }

    private void validateCreatePermission(String userRole, UUID hubId, UUID companyId) {

        switch (userRole) {
            case "MASTER":
                return;

            case "HUB_MANAGER":
                // 본인 허브에 대해서만 상품 생성 가능
                if (hubId == null) {
                    throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
                }
                return;

            case "COMPANY_MANAGER":
                // 본인 업체에 대해서만 상품 생성 가능
                if (companyId == null) {
                    throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
                }
                return;

            default:
                throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    private void validateUpdatePermission(Product product, String userRole, UUID hubId, UUID companyId) {

        switch (userRole) {
            case "MASTER":
                return;

            case "HUB_MANAGER":
                // 본인 허브 상품만 수정 가능
                if (hubId == null || !hubId.equals(product.getHubId())) {
                    throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
                }
                return;

            case "COMPANY_MANAGER":
                // 본인 회사 상품만 수정 가능
                if (companyId == null || !companyId.equals(product.getCompanyId())) {
                    throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
                }
                return;

            default:
                throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    private void validateDeletePermission(Product product, String userRole, UUID hubId) {

        switch (userRole) {
            case "MASTER":
                return;

            case "HUB_MANAGER":
                // 본인 허브 상품만 삭제 가능
                if (hubId == null || !hubId.equals(product.getHubId())) {
                    throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
                }
                return;

            default:
                throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }
    }

    //=====주문 서비스 연동한 재고 관리 로직=====//
    //재고 감소(주문 발생 시)
    @Override
    public void decreaseStock(UUID productId, Long amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (!product.isActive()) {
            throw new AppException(ProductErrorCode.PRODUCT_ACCESS_DENIED);
        }

        product.decreaseProductQuantity(amount);
    }

}
