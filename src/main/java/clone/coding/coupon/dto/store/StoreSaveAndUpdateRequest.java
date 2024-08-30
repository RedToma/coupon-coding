package clone.coding.coupon.dto.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreSaveAndUpdateRequest {

    @NotBlank(message = "매장 이름을 입력해 주세요.")
    private String storeName;

    @Pattern(regexp = "^070-\\d{3,4}-\\d{3,4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "매장 전화번호를 입력해 주세요")
    private String storeNum;

    @NotBlank(message = "매장 주소를 입력해 주세요")
    private String address;
}
