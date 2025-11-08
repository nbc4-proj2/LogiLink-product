package product_service.product.common;

import com.sparta.logilinkcommon.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode{
    PRODUCT_NOT_FOUND("PRODUCT0001", "상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    PROCESS_ACCESS_DENIED("PRODUCT0002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PRODUCT_DELETE_FORBIDDEN("PRODUCT0003", "상품을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_QUANTITY_INCREASE("PRODUCT0004", "추가 수량은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY_DECREASE("PRODUCT0005", "감소 수량은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("PRODUCT0006", "재고가 부족합니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    ProductErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
