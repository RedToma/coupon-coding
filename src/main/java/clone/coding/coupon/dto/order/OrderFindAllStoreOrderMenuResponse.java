package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderFindAllStoreOrderMenuResponse {

    private String menuName;

    private int menuCnt;

    public OrderFindAllStoreOrderMenuResponse(OrderMenu orderMenu) {
        this.menuName = orderMenu.getMenu().getMenuName();
        this.menuCnt = orderMenu.getMenuCnt();
    }
}
