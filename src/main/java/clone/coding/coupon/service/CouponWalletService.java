package clone.coding.coupon.service;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.dto.couponwallet.CouponWalletFindAllResponse;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.global.exception.error.ErrorCode;
import clone.coding.coupon.repository.CouponRepository;
import clone.coding.coupon.repository.CouponWalletRepository;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.error.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponWalletService {

    private final CouponRepository couponRepository;
    private final CouponRedemption couponRedemption;
    private final CustomerRepository customerRepository;
    private final CouponWalletRepository couponWalletRepository;


    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void addCouponWallet(String email, Long couponId) {
        Customer customer = findCustomer(email);
        Coupon coupon = findCoupon(couponId);

        if (!couponExpirationDateCheck(coupon)) {
            coupon.couponExpired();
            throw new ResourceNotFoundException(ERROR_COUPON_ISSUE_PERIOD);
        }

        if (!couponQuantityCheck(coupon)) {
            throw new ResourceNotFoundException(ERROR_COUPON_QUANTITY_EXCEEDED);
        }

        if (!couponsNumberCheck(customer, coupon)) {
            throw new ResourceNotFoundException(ERROR_COUPON_NO_LONGER_ISSUABLE);
        }

        coupon.couponIssuedComplete();

        CouponWallet couponWallet = CouponWallet.builder()
                .useYn(false)
                .startAt(LocalDateTime.now().withNano(0))
                .expiredAt(coupon.getExpiredAt())
                .couponCode(coupon.getPromotionCode())
                .coupon(coupon)
                .build();

        customer.addCouponWallets(couponWallet);
        couponWalletRepository.save(couponWallet);
    }

    @Transactional
    public void addCouponCode(CouponSaveRequest couponSaveRequest, Long couponQuantity) {
        Coupon coupon = couponRedemption.adminCouponIssuing(couponSaveRequest);
        couponRepository.save(coupon);
        couponCodeCreate(coupon, couponQuantity);
    }

    @Transactional
    public void modifyCouponCode(String email, String couponCode) {
        CouponWallet couponWallet = couponWalletRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_COUPON_NOT_FOUND));
        Coupon coupon = findCoupon(couponWallet.getCoupon().getId());
        Customer customer = findCustomer(email);

        if (!couponsNumberCheck(customer, coupon)) {
            throw new ResourceNotFoundException(ERROR_COUPON_NO_LONGER_ISSUABLE);
        }

        couponWallet.customerInfoUpdate(customer);
    }

    public List<CouponWalletFindAllResponse> findCouponWallet(String email) {
        Customer customer = findCustomer(email);

        return customer.getCouponWallets().stream()
                .filter(couponWallet -> !couponWallet.isUseYn())
                .map(CouponWalletFindAllResponse::new)
                .collect(Collectors.toList());
    }

    private void couponCodeCreate(Coupon coupon, Long couponQuantity) {
        for (int i = 0; i < couponQuantity; i++) {
            CouponWallet couponWallet = CouponWallet.builder()
                    .useYn(false)
                    .startAt(coupon.getStartAt())
                    .expiredAt(coupon.getExpiredAt())
                    .couponCode(UUID.randomUUID().toString())
                    .coupon(coupon)
                    .build();

            couponWalletRepository.save(couponWallet);
        }
    }


    private boolean couponExpirationDateCheck(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        return coupon.isAvailable() && (now.isAfter(coupon.getStartAt()) && now.isBefore(coupon.getExpiredAt()));
    }

    private boolean couponQuantityCheck(Coupon coupon) {
        return coupon.isAvailable() && (coupon.getMaxCnt() > coupon.getAllocatedCnt());
    }

    private boolean couponsNumberCheck(Customer customer, Coupon coupon) {
        int quantity = couponWalletRepository.couponsNumberCheck(customer.getId(), coupon.getId());
        return coupon.getMaxCntPerCus() > quantity;
    }

    private Customer findCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MEMBER_NOT_FOUND));
    }

    private Coupon findCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_COUPON_NOT_FOUND));
    }
}
