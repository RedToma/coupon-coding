package clone.coding.coupon.entity.coupon;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponWallet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_coupon_id", updatable = false)
    private Long id;

    private boolean useYn;

    private LocalDateTime usedAt;

    private LocalDateTime startAt;

    private LocalDateTime expiredAt;

    @Column(unique = true)
    private String couponCode;
}
