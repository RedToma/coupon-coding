package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.CouponWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CouponWalletRepository extends JpaRepository<CouponWallet, Long> {

    @Query("SELECT COUNT(*) FROM CouponWallet cw WHERE cw.customer.id = :customerId AND cw.coupon.id = :couponId AND cw.useYn = false ")
    int couponsNumberCheck(@Param("customerId") Long customerId, @Param("couponId") Long couponId);
}
