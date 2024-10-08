package clone.coding.coupon.entity.customer;

import clone.coding.coupon.entity.BaseTimeEntity;
import clone.coding.coupon.entity.store.Menu;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_menu_id", updatable = false)
    private Long id;

    private int menuCnt;

    private int menuPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public void changeMenuCnt(int cnt) {
        menuCnt = cnt;
    }

    public void orderStatusToOrder() {
        orderStatus = OrderStatus.ORDER;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
