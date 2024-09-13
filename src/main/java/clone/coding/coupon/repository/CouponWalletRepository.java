package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.CouponWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CouponWalletRepository extends JpaRepository<CouponWallet, Long> {

    @Query("SELECT COUNT(*) FROM CouponWallet cw WHERE cw.customer.id = :customerId AND cw.coupon.id = :couponId")
    int couponsNumberCheck(@Param("customerId") Long customerId, @Param("couponId") Long couponId);

    @Query("SELECT cw FROM CouponWallet cw WHERE cw.coupon.id IN :couponIds")
    List<CouponWallet> findByCouponIds(@Param("couponIds") List<Long> couponIds);

    @Modifying
    @Query("DELETE FROM CouponWallet cw WHERE cw.customer.id = :customerId")
    void deleteByCustomerCouponWallet(@Param("customerId") Long customerId);

    Optional<CouponWallet> findByCouponCode(String couponCode);
}
