package clone.coding.coupon.service;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.entity.admin.Admin;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.TimePolicy;
import clone.coding.coupon.entity.store.Store;
import clone.coding.coupon.repository.AdminRepository;
import clone.coding.coupon.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CouponRedemption {

    private final AdminRepository adminRepository;
    private final StoreRepository storeRepository;


    public Coupon adminCouponIssuing(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = null;
        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = adminRepository.findById(couponSaveRequest.getIssuerCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        if (admin.getAdminType().contains("BAMIN")) {
            coupon = Coupon.builder()
                    .name(couponSaveRequest.getName())
                    .amount(couponSaveRequest.getAmountOrRate())
                    .startAt(couponSaveRequest.getStartAt())
                    .expiredAt(couponSaveRequest.getExpiredAt())
                    .timePolicy(timePolicy)
                    .minOrderPrice(couponSaveRequest.getMinOrderPrice())
                    .discountType(couponSaveRequest.getDiscountType())
                    .issuerType(couponSaveRequest.getIssuerType())
                    .issuerCode(couponSaveRequest.getIssuerCode())
                    .maxCnt(couponSaveRequest.getMaxCnt())
                    .maxCntPerCus(couponSaveRequest.getMaxCntPerCus())
                    .promotionCode(String.valueOf(UUID.randomUUID()))
                    .allocatedCnt(0)
                    .available(true)
                    .build();
        } else {
            throw new IllegalArgumentException("어드민 쿠폰 생성 권한이 없습니다.");
        }

        return coupon;
    }

    public Coupon brandCouponIssuing(CouponSaveRequest couponSaveRequest) {
        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = adminRepository.findById(couponSaveRequest.getIssuerCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        return Coupon.builder()
                .name(couponSaveRequest.getName())
                .amount(couponSaveRequest.getAmountOrRate())
                .startAt(couponSaveRequest.getStartAt())
                .expiredAt(couponSaveRequest.getExpiredAt())
                .timePolicy(timePolicy)
                .minOrderPrice(couponSaveRequest.getMinOrderPrice())
                .discountType(couponSaveRequest.getDiscountType())
                .issuerType(couponSaveRequest.getIssuerType())
                .issuerCode(admin.getId())
                .brandId(admin.getBrandId())
                .maxCnt(couponSaveRequest.getMaxCnt())
                .maxCntPerCus(couponSaveRequest.getMaxCntPerCus())
                .promotionCode(String.valueOf(UUID.randomUUID()))
                .allocatedCnt(0)
                .available(true)
                .build();
    }

    public Coupon storeCouponIssuing(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = null;
        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = adminRepository.findById(couponSaveRequest.getIssuerCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
        Store store = storeRepository.findByStoreName(couponSaveRequest.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지점입니다."));

        if (admin.getBrandId() == store.getBrand().getId() || admin.getAdminType().contains("BAMIN")) {
            coupon = Coupon.builder()
                    .name(couponSaveRequest.getName())
                    .amount(couponSaveRequest.getAmountOrRate())
                    .startAt(couponSaveRequest.getStartAt())
                    .expiredAt(couponSaveRequest.getExpiredAt())
                    .timePolicy(timePolicy)
                    .minOrderPrice(couponSaveRequest.getMinOrderPrice())
                    .discountType(couponSaveRequest.getDiscountType())
                    .issuerType(couponSaveRequest.getIssuerType())
                    .issuerCode(admin.getId())
                    .brandId(admin.getBrandId())
                    .storeId(store.getId())
                    .maxCnt(couponSaveRequest.getMaxCnt())
                    .maxCntPerCus(couponSaveRequest.getMaxCntPerCus())
                    .promotionCode(String.valueOf(UUID.randomUUID()))
                    .allocatedCnt(0)
                    .available(true)
                    .build();
        } else {
            throw new IllegalArgumentException("지점 쿠폰 생성 권한이 없습니다.");
        }

        return coupon;
    }


    private TimePolicy timePolicySet(CouponSaveRequest couponSaveRequest) {
        TimePolicy timePolicy;

        if (couponSaveRequest.isPolicyStatus()) timePolicy = new TimePolicy(true, couponSaveRequest.getStartTime(), couponSaveRequest.getEndTime());
        else timePolicy = new TimePolicy(false, null, null);

        return timePolicy;
    }
}
