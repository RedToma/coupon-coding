package clone.coding.coupon.global.exception.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ERROR_MEMBER_NOT_FOUND("ERROR_MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    ERROR_EMAIL_DUPLICATION("ERROR_EMAIL_DUPLICATION", "이메일이 중복됩니다."),
    ERROR_NICKNAME_DUPLICATION("ERROR_NICKNAME_DUPLICATION", "닉네임이 중복됩니다."),
    ERROR_BRAND_NOT_FOUND("ERROR_BRAND_NOT_FOUND", "브랜드를 찾을 수 없습니다."),
    ERROR_STORE_NOT_FOUND("ERROR_STORE_NOT_FOUND", "지점을 찾을 수 없습니다."),
    ERROR_MENU_NOT_FOUND("ERROR_MENU_NOT_FOUND", "존재하지 않는 메뉴입니다."),
    ERROR_ORDER_MENU_NOT_FOUND("ERROR_ORDER_MENU_NOT_FOUND", "존재하지 않는 장바구니 목록입니다."),
    ERROR_ORDER_MENU_EMPTY("ERROR_ORDER_MENU_EMPTY", "장바구니가 비어있습니다 주문할 수 없습니다."),
    ERROR_ORDER_NOT_FOUND("ERROR_ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."),
    ERROR_ORDER_CANNOT_CANCELLED("ERROR_ORDER_CANNOT_CANCELLED", "주문을 취소할 수 없습니다."),
    ERROR_ORDER_ALREADY_CANCELLED("ERROR_ORDER_ALREADY_CANCELLED", "이미 취소된 주문 입니다."),
    ERROR_CANNOT_CHANGE_STATUS("ERROR_CANNOT_CHANGE_STATUS", "상태를 변경할 수 없습니다."),
    ERROR_COUPON_NOT_FOUND("ERROR_COUPON_NOT_FOUND", "존재하지 않는 쿠폰입니다."),
    ERROR_NO_COUPON_CREATION_PERMISSION("ERROR_NO_COUPON_CREATION_PERMISSION", "쿠폰 생성 권한이 없습니다."),
    ERROR_COUPON_ISSUE_PERIOD("ERROR_COUPON_ISSUE_PERIOD", "쿠폰 발급 가능기간이 아닙니다."),
    ERROR_COUPON_QUANTITY_EXCEEDED("ERROR_COUPON_QUANTITY_EXCEEDED", "쿠폰 수량이 마감되었습니다."),
    ERROR_COUPON_NO_LONGER_ISSUABLE("ERROR_COUPON_NO_LONGER_ISSUABLE", "쿠폰을 더 이상 발급받을 수 없습니다."),
    ERROR_ADMIN_NOT_FOUND("ERROR_ADMIN_NOT_FOUND", "존재하지 않는 관리자입니다.");


    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
