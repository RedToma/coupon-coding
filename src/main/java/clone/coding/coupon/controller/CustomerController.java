package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.CustomerSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 회원가입
     * @param customerSaveRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/customer/sign-up")
    public ApiResponse<Object> customerSave(@Valid @RequestBody CustomerSaveRequest customerSaveRequest, BindingResult bindingResult) {
        customerService.addCustomer(customerSaveRequest);
        return ApiResponse.success("회원가입이 완료되었습니다.");
    }

    /**
     * 이메일 중복 확인
     * @param email
     * @return
     */
    @GetMapping("/customer/sign-up-email/{email}")
    public ApiResponse<?> customerCheckEmailDuplication(@PathVariable String email) {
        if (customerService.checkEmailDuplication(email)) return ApiResponse.error("DUPLICATION", "이메일이 중복됩니다.");
        return ApiResponse.success("사용 가능한 이메일 입니다.");
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return
     */
    @GetMapping("/customer/sign-up-nickname/{nickname}")
    public ApiResponse<?> customerCheckNicknameDuplication(@PathVariable String nickname) {
        if (customerService.checkNicknameDuplication(nickname)) return ApiResponse.error("DUPLICATION", "닉네임이 중복됩니다.");
        return ApiResponse.success("사용 가능한 닉네임 입니다.");
    }


    /**
     * 비밀번호 변경
     * @param customerPwUpdateRequest
     * @param bindingResult
     * @param id
     * @return
     */
    @PatchMapping("/customer/info/pw-change/{id}")
    public ApiResponse<Object> customerPwModify(@Valid @RequestBody CustomerPwUpdateRequest customerPwUpdateRequest,
                                                BindingResult bindingResult,
                                                @PathVariable Long id) {
        customerService.modifyCustomerPw(customerPwUpdateRequest, id);
        return ApiResponse.success("비밀번호가 변경되었습니다.");
    }
}
