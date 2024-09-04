package clone.coding.coupon.entity.coupon;


import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    //admin_id
    private Long issuerCode;

    //issuerType brand일때 해당 브랜드에서만 쿠폰 사용가능하게
    private Long brandId;

    //issuerType store일때 해당 지점에서만 쿠폰 사용가능하게
    private Long storeId;

    private int maxCnt;

    private int maxCntPerCus;

    @Column(unique = true)
    private String promotionCode;

    private int allocatedCnt;
}
