package clone.coding.coupon.entity.coupon;


import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_id", updatable = false)
    private Long id;

    private String name;

    private int amount;

    private LocalDateTime startAt;

    private LocalDateTime expiredAt;

    @Embedded
    private TimePolicy timePolicy;

    private Long minOrderPrice;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    private IssuerType issuerType;

    private Long issuerCode;

    private Long brandId;

    private Long storeId;

    private int maxCnt;

    private int maxCntPerCus;

    @Column(unique = true)
    private String promotionCode;

    private int allocatedCnt;

    private boolean available;

    public void couponExpired() {
        available = false;
    }

    public void couponIssuedComplete() {
        allocatedCnt++;
    }

    public void disableCouponUsage() {
        available = false;
        issuerCode = null;
    }
}
