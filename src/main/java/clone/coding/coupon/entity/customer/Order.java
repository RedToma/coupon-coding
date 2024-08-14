package clone.coding.coupon.entity.customer;

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
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", updatable = false)
    private Long id;

    private String payment;

    private int totalAmount;

    private int discount;

    private String status;

    private LocalDateTime orderTime;

    private LocalDateTime arrivalTime;
}
