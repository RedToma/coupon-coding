package clone.coding.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerSaveRequest {

    @Pattern(regexp = "^[A-Za-z0-9_-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=?<>]).{8,20}$", message = "비밀번호는 8자 이상 20자 이하이어야 하며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 하나씩 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,20}$", message = "2자 이상 20자 상하 한글 또는 영문 이용 닉네임을 설정해 주세요.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    private String address;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    private String phoneNum;
}
