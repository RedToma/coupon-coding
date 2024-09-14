package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.Order;
import clone.coding.coupon.entity.customer.PaymentType;
import clone.coding.coupon.entity.customer.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFindAllByStoreResponse {

    private PaymentType paymentType;

    private List<OrderFindAllStoreOrderMenuResponse> orderMenuList;

    private LocalDateTime orderTime;

    private StatusType statusType;

    private int totalAmount;

    private int discount;

    public OrderFindAllByStoreResponse(Order order) {
        this.paymentType = order.getPaymentType();
        this.orderMenuList = order.getOrderMenus().stream()
                .map(OrderFindAllStoreOrderMenuResponse::new)
                .collect(Collectors.toList());
        this.orderTime = order.getOrderTime();
        this.statusType = order.getStatusType();
        this.totalAmount = order.getTotalAmount();
        this.discount = order.getDiscount();
    }
}
