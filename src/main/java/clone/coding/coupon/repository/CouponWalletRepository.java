package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.CouponWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CouponWalletRepository extends JpaRepository<CouponWallet, Long> {

    @Query("SELECT COUNT(*) FROM CouponWallet cw WHERE cw.customer.id = :customerId AND cw.coupon.id = :couponId") // 사용했건 안했건 일단 해당 쿠폰을 받은적 있는지 없는지 확인해야 하는 것이 아닌지?
    int couponsNumberCheck(@Param("customerId") Long customerId, @Param("couponId") Long couponId);

    Optional<CouponWallet> findByCouponCode(String couponCode);
}
