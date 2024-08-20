package clone.coding.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerPwUpdateRequest {

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=?<>]).{8,20}$", message = "비밀번호는 8자 이상 20자 이하이어야 하며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 하나씩 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
