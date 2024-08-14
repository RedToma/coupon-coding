package clone.coding.coupon.entity;


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
public class Coupons extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_id", updatable = false)
    private Long id;

    private String name;

    private int amount;

    private LocalDateTime startAt;

    private LocalDateTime expiredAt;

    private Long minOrderPrice;

    private String discountType;

    private String issuerType;

    private Long issuerCode;

    private int maxCnt;

    private int maxCntPerCus;

    private String promotionCode;

    private int allocatedCnt;
}
