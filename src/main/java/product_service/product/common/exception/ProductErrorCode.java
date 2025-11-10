package product_service.product.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode{
    PRODUCT_NOT_FOUND("PRODUCT0001", "상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    PRODUCT_NAME_DUPLICATE("PRODUCT0002", "이미 있는 상품명입니다.", HttpStatus.CONFLICT),
    PROCESS_ACCESS_DENIED("PRODUCT0003", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PRODUCT_DELETE_FORBIDDEN("PRODUCT0004", "상품을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_QUANTITY_INCREASE("PRODUCT0005", "추가 수량은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY_DECREASE("PRODUCT0006", "감소 수량은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("PRODUCT0007", "재고가 부족합니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    ProductErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
