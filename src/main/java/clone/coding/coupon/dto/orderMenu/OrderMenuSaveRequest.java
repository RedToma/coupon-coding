package clone.coding.coupon.dto.orderMenu;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderMenuSaveRequest {

    @NotNull(message = "menuID를 입력해 주세요.")
    private Long menuId;

    @Min(value = 1, message = "한개 이상부터 주문 가능합니다.")
    @Max(value = 99, message = "더 이상 주문할 수 없습니다.")
    private int menuCnt;
}