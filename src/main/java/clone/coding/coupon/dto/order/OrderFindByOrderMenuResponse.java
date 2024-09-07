package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderFindByOrderMenuResponse {

    private String menuName;

    private int menuPrice;

    public OrderFindByOrderMenuResponse(OrderMenu orderMenu) {
        this.menuName = orderMenu.getMenu().getMenuName();
        this.menuPrice = orderMenu.getMenu().getPrice();
    }
}
