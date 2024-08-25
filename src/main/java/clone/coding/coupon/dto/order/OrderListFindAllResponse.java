package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.Order;
import clone.coding.coupon.entity.customer.StatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderListFindAllResponse {

    private int totalAmount;

    private StatusType statusType;

    private String orderTime;

    private List<OrderFindByMenuNameResponse> menuAndStoreList;

    public OrderListFindAllResponse(Order order) {
        totalAmount = order.getTotalAmount();
        statusType = order.getStatusType();
        orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        menuAndStoreList = order.getOrderMenus().stream()
                .map(OrderFindByMenuNameResponse::new)
                .collect(Collectors.toList());
    }
}
