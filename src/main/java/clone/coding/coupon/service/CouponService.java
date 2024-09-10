package clone.coding.coupon.service;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final CouponRedemption couponRedemption;
    private final CouponRepository couponRepository;

    @Transactional
    public void addAdminCoupon(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = couponRedemption.adminCouponIssuing(couponSaveRequest);
        couponRepository.save(coupon);
    }

    @Transactional
    public void addBrandCoupon(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = couponRedemption.brandCouponIssuing(couponSaveRequest);
        couponRepository.save(coupon);
    }

    @Transactional
    public void addStoreCoupon(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = couponRedemption.storeCouponIssuing(couponSaveRequest);
        couponRepository.save(coupon);
    }
}
