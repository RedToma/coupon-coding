package clone.coding.coupon.dto.orderMenu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class OrderMenuSaveRequest {

    @NotNull(message = "menuID를 입력해 주세요.")
    private Long menuId;

    @NotNull(message = "메뉴 수량을 입력해 주세요.")
    @Range(min = 1, max = 99)
    private int menuCnt;

    @NotNull(message = "메뉴 가격을 입력해 주세요.")
    @Min(100)
    private int menuPrice;
}
