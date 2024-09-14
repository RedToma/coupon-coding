package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.Order;
import clone.coding.coupon.entity.customer.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListFindAllResponse {

    private int totalAmount;

    private StatusType statusType;

    private String orderTime;

    private String storeName;

    private List<OrderFindByMenuNameResponse> menuList;

    public OrderListFindAllResponse(Order order) {
        totalAmount = order.getTotalAmount();
        statusType = order.getStatusType();
        orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        storeName = order.getStore().getStoreName();
        menuList = order.getOrderMenus().stream()
                .map(OrderFindByMenuNameResponse::new)
                .collect(Collectors.toList());
    }
}
