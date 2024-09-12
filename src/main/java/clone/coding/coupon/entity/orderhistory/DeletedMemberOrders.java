package clone.coding.coupon.entity.orderhistory;

import clone.coding.coupon.entity.customer.PaymentType;
import clone.coding.coupon.entity.customer.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletedMemberOrders {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delete_member_orders_id", updatable = false)
    private Long id;

    private String email;

    private String phoneNum;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private int totalAmount;

    private int discount;

    private LocalDateTime orderTime;

    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    private String couponName;

    private String promotionCode;
}
