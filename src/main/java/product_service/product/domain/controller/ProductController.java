package product_service.product.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import product_service.product.common.BaseResponse;
import product_service.product.common.PageableUtils;
import product_service.product.domain.model.dto.request.ProductCreateReq;
import product_service.product.domain.model.dto.request.ProductUpdateReq;
import product_service.product.domain.model.dto.response.ProductRes;
import product_service.product.domain.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

    // 상품 생성 - MASTER, HUB, COMPANY
    @PostMapping
    public BaseResponse<ProductRes> createProduct(
            @RequestBody @Valid ProductCreateReq requestDto,
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Role") String userRole,
            @RequestHeader(value = "X-Hub_Id") UUID hubId,
            @RequestHeader(value = "X-Company-Id") UUID companyId){
        ProductRes responseDto = productService.createProduct(requestDto, userId, userRole, hubId, companyId);
        return BaseResponse.success(responseDto);
    }

    // 상품 수정 - MASTER, HUB, COMPANY
    @PutMapping("/{productId}")
    public BaseResponse<ProductRes> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProductUpdateReq requestDto,
            @RequestHeader(value="X-User-Id") Long userId,
            @RequestHeader(value="X-User-Role") String userRole,
            @RequestHeader(value = "X-Hub_Id") UUID hubId,
            @RequestHeader(value = "X-Company-Id") UUID companyId){
        ProductRes responseDto = productService.updateProduct(productId, requestDto, userId, userRole, hubId, companyId);
        return BaseResponse.success(responseDto);
    }

    // 상품 소프트 삭제 - MASTER, HUB
    @DeleteMapping("/{productId}")
    public BaseResponse<ProductRes> deleteProduct(
            @PathVariable UUID productId,
            @RequestHeader(value="X-User-Id") Long userId,
            @RequestHeader(value="X-User-Role") String userRole,
            @RequestHeader(value = "X-Hub_Id") UUID hubId,
            @RequestHeader(value = "X-Company-Id") UUID companyId){
        productService.deleteProduct(productId, userId, userRole, hubId, companyId);
        return BaseResponse.success(null);
    }

    // 상품 단건 조회 - ALL
    @GetMapping("/{productId}")
    public BaseResponse<ProductRes> getProduct(@PathVariable UUID productId){
        return BaseResponse.success(productService.getProduct(productId));
    }

    // 상품 목록 조회 - ALL
    @GetMapping
    public BaseResponse<Page<ProductRes>> getProductPage(Pageable pageable){
        Pageable p = PageableUtils.enforce(pageable);
        Page<ProductRes> result = productService.getProductPage(p);
        return BaseResponse.success(result);
    }

    // 상품 재고 감소(Order-service 호출 시)
    @PutMapping("/{productId}/decrease-stock")
    public BaseResponse<String> decreaseStock(@PathVariable UUID productId, @RequestParam Long amount) {
        productService.decreaseStock(productId, amount);
        return BaseResponse.success("재고 감소 완료");
    }

}
