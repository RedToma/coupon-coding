package clone.coding.coupon.dto.couponwallet;

import clone.coding.coupon.entity.coupon.CouponWallet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponWalletFindAllResponse {

    private int amount;

    private String name;

    private Long minOrderPrice;

    private Long leftDays;

    private LocalDateTime expiredAt;

    public CouponWalletFindAllResponse(CouponWallet couponWallet) {
        this.amount = couponWallet.getCoupon().getAmount();
        this.name = couponWallet.getCoupon().getName();
        this.minOrderPrice = couponWallet.getCoupon().getMinOrderPrice();
        this.leftDays = ChronoUnit.DAYS.between(LocalDateTime.now(), couponWallet.getCoupon().getExpiredAt());
        this.expiredAt = couponWallet.getCoupon().getExpiredAt();
    }
}
