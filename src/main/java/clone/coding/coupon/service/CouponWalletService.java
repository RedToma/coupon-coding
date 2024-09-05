package clone.coding.coupon.service;

import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CouponRepository;
import clone.coding.coupon.repository.CouponWalletRepository;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponWalletService {

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final CouponWalletRepository couponWalletRepository;


    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void addCouponWallet(String email, Long couponId) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!couponExpirationDateCheck(coupon)) {
            coupon.couponExpired();
            throw new IllegalArgumentException("쿠폰 발급 가능기간이 아닙니다.");
        }

        if (!couponExpirationTimeCheck(coupon)) {
            throw new IllegalArgumentException("쿠폰 발급 가능시간이 아닙니다.");
        }

        if (!couponQuantityCheck(coupon)) {
            throw new IllegalArgumentException("쿠폰 수량이 마감되었습니다.");
        }

        if (!couponsNumberCheck(customer, coupon)) {
            throw new IllegalArgumentException("쿠폰을 더 이상 발급받을 수 없습니다.");
        }

        coupon.couponIssuedComplete();

        CouponWallet couponWallet = CouponWallet.builder()
                .useYn(false)
                .startAt(coupon.getStartAt())
                .expiredAt(coupon.getExpiredAt())
                .couponCode(coupon.getPromotionCode())
                .coupon(coupon)
                .build();

        customer.addCouponWallets(couponWallet);
        couponWalletRepository.save(couponWallet);
    }


    private boolean couponExpirationDateCheck(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();

        if (coupon.isAvailable() && (now.isAfter(coupon.getStartAt()) && now.isBefore(coupon.getExpiredAt()))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean couponExpirationTimeCheck(Coupon coupon) {
        LocalTime now = LocalTime.now();

        if (coupon.isAvailable() && (now.isAfter(coupon.getTimePolicy().getStartTime()) && now.isBefore(coupon.getTimePolicy().getEndTime()))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean couponQuantityCheck(Coupon coupon) {
        if (coupon.isAvailable() && (coupon.getMaxCnt() > coupon.getAllocatedCnt())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean couponsNumberCheck(Customer customer, Coupon coupon) {
        int quantity = couponWalletRepository.couponsNumberCheck(customer.getId(), coupon.getId());

        if (coupon.getMaxCntPerCus() > quantity) {
            return true;
        } else {
            return false;
        }
    }
}
