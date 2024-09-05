package clone.coding.coupon.dto.coupon;

import clone.coding.coupon.entity.coupon.DiscountType;
import clone.coding.coupon.entity.coupon.IssuerType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponSaveRequest {

    private Long issuerCode;

    private String storeName;

    @NotNull(message = "쿠폰 이름을 입력해주세요.")
    private String name;

    private int amountOrRate;

    @NotNull(message = "쿠폰 사용가능 시작일자를 입력해주세요. (년-월-일)")
    private LocalDateTime startAt;

    @NotNull(message = "쿠폰 사용가능 종료일자를 입력해주세요. (년-월-일)")
    private LocalDateTime expiredAt;

    private boolean policyStatus;

    private LocalTime startTime;

    private LocalTime endTime;

    @Min(value = 0, message = "최소 주문 금액은 0원 이상 설정 가능합니다.")
    private Long minOrderPrice;

    @NotNull(message = "할인 타입을 선택해주세요.")
    private DiscountType discountType;

    @NotNull(message = "발급 주체 타입을 선택해주세요.")
    private IssuerType issuerType;

    private int maxCnt;

    private int maxCntPerCus;
}
