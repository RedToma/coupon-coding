package clone.coding.coupon.service;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.dto.couponwallet.CouponWalletFindAllResponse;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CouponRepository;
import clone.coding.coupon.repository.CouponWalletRepository;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!couponExpirationDateCheck(coupon)) {
            coupon.couponExpired();
            throw new IllegalArgumentException("쿠폰 발급 가능기간이 아닙니다.");
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
                .startAt(LocalDateTime.now()) // 발급받은 시간을 넣는게 맞을지 아니면 쿠폰 테이블 내 시작일자를 넣는게 맞을지?
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
    public void modifyCouponCode(@AuthenticationPrincipal UserDetails userDetails, String couponCode) {
        CouponWallet couponWallet = couponWalletRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰번호 입니다."));
        Coupon coupon = couponRepository.findById(couponWallet.getCoupon().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        Customer customer = customerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!couponsNumberCheck(customer, coupon)) {
            throw new IllegalArgumentException("쿠폰을 더 이상 발급받을 수 없습니다.");
        }

        couponWallet.customerInfoUpdate(customer);
    }

    public List<CouponWalletFindAllResponse> findCouponWallet(String email) { //이름, 할인금액, 최소주문금액, (브랜드 or 매장주체), 시작일자, 만료일자
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

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

        if (coupon.isAvailable() && (now.isAfter(coupon.getStartAt()) && now.isBefore(coupon.getExpiredAt()))) {
            return true;
        } else {
            return false;
        }
    }

//    private boolean couponExpirationTimeCheck(Coupon coupon) {
//        LocalTime now = LocalTime.now();
//
//        if (coupon.isAvailable() && (now.isAfter(coupon.getTimePolicy().getStartTime()) && now.isBefore(coupon.getTimePolicy().getEndTime()))) {
//            return true;
//        } else {
//            return false;
//        }
//    }

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
