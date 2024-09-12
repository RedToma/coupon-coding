package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.issuerCode = :issuerCode")
    List<Coupon> findCouponsByIssuerCode(Long issuerCode);

    @Query("SELECT c FROM Coupon c WHERE c.storeId = :storeId")
    List<Coupon> findCouponsByStoreId(Long storeId);
}
