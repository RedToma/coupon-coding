package clone.coding.coupon.entity.customer;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private int totalAmount;

    private int discount;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    private LocalDateTime orderTime;

    private LocalDateTime arrivalTime;

    @OneToMany(mappedBy = "order")
    @Builder.Default
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void addOrderMenus(OrderMenu orderMenu) {
        orderMenus.add(orderMenu);
        orderMenu.setOrder(this);
    }
}
