package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.coupon.CouponWallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFindByCouponResponse {

    private Long couponWalletId;

    private int amount;

    private String couponName;

    private LocalDateTime expireAt;

    private Long minOrderPrice;

    public OrderFindByCouponResponse(CouponWallet couponWallet) {
        this.couponWalletId = couponWallet.getId();
        this.amount = couponWallet.getCoupon().getAmount();
        this.couponName = couponWallet.getCoupon().getName();
        this.expireAt = couponWallet.getExpiredAt();
        this.minOrderPrice = couponWallet.getCoupon().getMinOrderPrice();
    }
}
