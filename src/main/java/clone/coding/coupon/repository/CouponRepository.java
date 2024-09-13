package clone.coding.coupon.repository;

import clone.coding.coupon.entity.coupon.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.issuerCode = :issuerCode")
    List<Coupon> findCouponsByIssuerCode(Long issuerCode);

    @Query("SELECT c FROM Coupon c WHERE c.storeId = :storeId")
    List<Coupon> findCouponsByStoreId(Long storeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    Optional<Coupon> findCouponById(Long couponId);
}
