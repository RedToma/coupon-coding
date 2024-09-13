package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFindByMenuNameResponse {

    private String menuName;

    public OrderFindByMenuNameResponse(OrderMenu orderMenu) {
        menuName = orderMenu.getMenu().getMenuName();
    }
}
