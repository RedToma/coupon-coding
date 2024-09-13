package clone.coding.coupon.dto.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuSaveAndUpdateRequest {

    @NotBlank(message = "메뉴 이름을 입력해 주세요.")
    private String menuName;

    @NotNull(message = "가격을 입력해 주세요.")
    private int price;

    @NotNull(message = "품절 여부를 선택해 주세요.")
    private boolean soldout;
}

