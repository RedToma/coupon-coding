package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderFindByMenuNameResponse {

    private String storeName;

    private String menuName;

    public OrderFindByMenuNameResponse(OrderMenu orderMenu) {
        storeName = orderMenu.getMenu().getStore().getStoreName();
        menuName = orderMenu.getMenu().getMenuName();
    }
}
