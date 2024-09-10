package clone.coding.coupon.entity.coupon;

import clone.coding.coupon.entity.BaseTimeEntity;
import clone.coding.coupon.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
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

    private String couponCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void customerInfoUpdate(Customer customer) {
        this.customer = customer;
    }

    public void couponUseProcess() {
        this.useYn = true;
        this.usedAt = LocalDateTime.now().withNano(0);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
