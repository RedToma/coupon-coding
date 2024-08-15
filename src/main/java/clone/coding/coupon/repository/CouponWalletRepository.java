package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.CouponWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponWalletRepository extends JpaRepository<CouponWallet, Long> {
}
