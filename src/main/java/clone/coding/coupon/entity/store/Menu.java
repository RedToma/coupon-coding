package clone.coding.coupon.entity.store;


import clone.coding.coupon.dto.menu.MenuSaveAndUpdateRequest;
import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_id", updatable = false)
    private Long id;

    private String menuName;

    private int price;

    private boolean soldout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void updateMenu(MenuSaveAndUpdateRequest menuSaveAndUpdateRequest) {
        menuName = menuSaveAndUpdateRequest.getMenuName();
        price = menuSaveAndUpdateRequest.getPrice();
        soldout = menuSaveAndUpdateRequest.isSoldout();
    }

    public void menuDelete() {
        soldout = true;
    }
}
