package clone.coding.coupon.dto.menu;

import clone.coding.coupon.entity.store.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuFindAllResponse {

    private String menuName;

    private int price;

    private boolean soldout;

    public MenuFindAllResponse(Menu menu) {
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.soldout = menu.isSoldout();
    }
}
