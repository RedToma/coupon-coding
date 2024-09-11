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

import static clone.coding.coupon.global.exception.ErrorMessage.*;

@Component
@RequiredArgsConstructor
public class CouponRedemption {

    private final AdminRepository adminRepository;
    private final StoreRepository storeRepository;


    public Coupon adminCouponIssuing(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = null;

        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = findAdmin(couponSaveRequest);

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
                    .promotionCode(UUID.randomUUID().toString())
                    .allocatedCnt(0)
                    .available(true)
                    .build();
        } else {
            throw new IllegalArgumentException(ERROR_NO_COUPON_CREATION_PERMISSION);
        }

        return coupon;
    }

    public Coupon brandCouponIssuing(CouponSaveRequest couponSaveRequest) {
        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = findAdmin(couponSaveRequest);

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
                .promotionCode(UUID.randomUUID().toString())
                .allocatedCnt(0)
                .available(true)
                .build();
    }

    public Coupon storeCouponIssuing(CouponSaveRequest couponSaveRequest) {
        Coupon coupon = null;

        TimePolicy timePolicy = timePolicySet(couponSaveRequest);
        Admin admin = findAdmin(couponSaveRequest);
        Store store = storeRepository.findByStoreName(couponSaveRequest.getStoreName())
                .orElseThrow(() -> new IllegalArgumentException(ERROR_STORE_NOT_FOUND));

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
                    .promotionCode(UUID.randomUUID().toString())
                    .allocatedCnt(0)
                    .available(true)
                    .build();
        } else {
            throw new IllegalArgumentException(ERROR_NO_COUPON_CREATION_PERMISSION);
        }

        return coupon;
    }


    private TimePolicy timePolicySet(CouponSaveRequest couponSaveRequest) {
        return new TimePolicy(couponSaveRequest.getStartTime(), couponSaveRequest.getEndTime());
    }

    private Admin findAdmin(CouponSaveRequest couponSaveRequest) {
        return adminRepository.findById(couponSaveRequest.getIssuerCode())
                .orElseThrow(() -> new IllegalArgumentException(ERROR_ADMIN_NOT_FOUND));
    }
}
