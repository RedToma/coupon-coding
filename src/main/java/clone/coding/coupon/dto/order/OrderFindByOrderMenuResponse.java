package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFindByOrderMenuResponse {

    private String menuName;

    private int menuCnt;

    private int menuPrice;

    public OrderFindByOrderMenuResponse(OrderMenu orderMenu) {
        this.menuName = orderMenu.getMenu().getMenuName();
        this.menuCnt = orderMenu.getMenuCnt();
        this.menuPrice = orderMenu.getMenu().getPrice();
    }
}
