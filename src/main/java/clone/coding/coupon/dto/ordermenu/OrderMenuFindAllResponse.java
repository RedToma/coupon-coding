package clone.coding.coupon.dto.ordermenu;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuFindAllResponse {

    private Long orderMenuId;

    private Long menuId;

    private String menuName;

    private int menuCnt;

    private int menuPrice;

    public OrderMenuFindAllResponse(OrderMenu orderMenu) {
        orderMenuId = orderMenu.getId();
        menuId = orderMenu.getMenu().getId();
        menuName = orderMenu.getMenu().getMenuName();
        menuCnt = orderMenu.getMenuCnt();
        menuPrice = orderMenu.getMenuCnt() * orderMenu.getMenuPrice();
    }
}
