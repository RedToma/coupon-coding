package clone.coding.coupon.service;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.TimePolicy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CouponRedemption {

    public Coupon adminCouponIssuing(CouponSaveRequest couponSaveRequest, Long adminId) {
        TimePolicy timePolicy = timePolicySet(couponSaveRequest);

        return Coupon.builder()
                .name(couponSaveRequest.getName())
                .amount(couponSaveRequest.getAmount())
                .startAt(couponSaveRequest.getStartAt())
                .expiredAt(couponSaveRequest.getExpiredAt())
                .timePolicy(timePolicy)
                .minOrderPrice(couponSaveRequest.getMinOrderPrice())
                .discountType(couponSaveRequest.getDiscountType())
                .issuerType(couponSaveRequest.getIssuerType())
                .issuerCode(adminId)
                .maxCnt(couponSaveRequest.getMaxCnt())
                .maxCntPerCus(couponSaveRequest.getMaxCntPerCus())
                .promotionCode(String.valueOf(UUID.randomUUID()))
                .allocatedCnt(0)
                .build();
    }


    private TimePolicy timePolicySet(CouponSaveRequest couponSaveRequest) {
        TimePolicy timePolicy;

        if (couponSaveRequest.isPolicyStatus()) timePolicy = new TimePolicy(true, couponSaveRequest.getStartTime(), couponSaveRequest.getEndTime());
        else timePolicy = new TimePolicy(false, null, null);

        return timePolicy;
    }
}
