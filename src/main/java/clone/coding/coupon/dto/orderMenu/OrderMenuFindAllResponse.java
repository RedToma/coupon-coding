package clone.coding.coupon.dto.orderMenu;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderMenuFindAllResponse {

    private Long orderMenuId;

    private Long menuId;

    private int menuCnt;

    private int menuPrice;

    public OrderMenuFindAllResponse(OrderMenu orderMenu) {
        orderMenuId = orderMenu.getId();
        menuId = orderMenu.getMenu().getId();
        menuCnt = orderMenu.getMenuCnt();
        menuPrice = orderMenu.getMenuPrice();
    }
}
