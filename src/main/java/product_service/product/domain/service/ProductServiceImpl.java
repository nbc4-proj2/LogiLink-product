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
    public ProductRes createProduct(ProductCreateReq requestDto, Long userId, String userRole){

        // 상품 생성 권한 검증
        //validateCreatePermission(requestDto, userId, userRole);

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
                .hubId(requestDto.getHubId())
                .companyId(requestDto.getCompanyId())
                .createdBy(userId)
                .build();
        return ProductRes.from(productRepository.save(product));
    }

    //====상품 수정(MASTER, HUB, COMPANY)=====//
    @Override
    @Transactional
    public ProductRes updateProduct(UUID productId, ProductUpdateReq requestDto, Long userId, String userRole){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 상품 수정 권한 검증
        //validateUpdatePermission(productId, requestDto, userId, userRole);

        // 상품 수정
        if(!product.getProductName().equals(requestDto.getProductName()) && productRepository.existsByProductName(requestDto.getProductName())){
            throw new AppException(ProductErrorCode.PRODUCT_NAME_DUPLICATE);
        }

        product.update(
                requestDto.getProductName(),
                requestDto.getProductDescription(),
                requestDto.getProductPrice(),
                requestDto.getProductQuantity(),
                ProductStatus.ACTIVE,
                requestDto.getHubId(),
                requestDto.getCompanyId()
        );
        return ProductRes.from(productRepository.save(product));
    }

    //====상품 삭제(MASTER, HUB)=====//
    @Override
    @Transactional
    public void deleteProduct(UUID productId, Long userId, String userRole){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 상품 삭제 권한 검증
        //validateDeletePermission(product, userRole, userId);

        product.delete();
    }

    //====상품 단건 조회(ALL)=====//
    @Override
    @Transactional(readOnly = true)
    public ProductRes getProduct(UUID productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductRes.from(product);
    }


    //====상품 목록 조회(ALL)=====//
    @Override
    @Transactional(readOnly = true)
    public Page<ProductRes> getProductPage(Pageable pageable){

        return productRepository.findAll(pageable).map(ProductRes::from);
    }
}
