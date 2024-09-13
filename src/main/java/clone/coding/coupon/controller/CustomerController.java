package clone.coding.coupon.controller;

import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
     *
     * @param customerSaveRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/customer/sign-up")
    public ApiResponse<Object> customerAdd(@Valid @RequestBody CustomerSaveRequest customerSaveRequest, BindingResult bindingResult) {
        customerService.addCustomer(customerSaveRequest);
        return ApiResponse.success("회원가입이 완료되었습니다.");
    }

    /**
     * 회원탈퇴
     *
     * @param userDetails
     * @return
     */
    @DeleteMapping("/customer/withdraw")
    public ApiResponse<Object> customerRemove(@AuthenticationPrincipal UserDetails userDetails) {
        customerService.removeCustomer(userDetails.getUsername());
        return ApiResponse.success("회원탈퇴 되었습니다.");
    }

    /**
     * 비밀번호 변경
     *
     * @param customerPwUpdateRequest
     * @param bindingResult
     * @param userDetails
     * @return
     */
    @PatchMapping("/customer/info/pw-change")
    public ApiResponse<Object> customerPwModify(@Valid @RequestBody CustomerPwUpdateRequest customerPwUpdateRequest,
                                                BindingResult bindingResult,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        customerService.modifyCustomerPw(customerPwUpdateRequest, userDetails.getUsername());
        return ApiResponse.success("비밀번호가 변경되었습니다.");
    }

    /**
     * 주소 변경
     *
     * @param address
     * @param userDetails
     * @return
     */
    @PatchMapping("/customer/info/address")
    public ApiResponse<Object> customerAddressModify(@RequestParam String address, @AuthenticationPrincipal UserDetails userDetails) {
        customerService.modifyCustomerAddress(address, userDetails.getUsername());
        return ApiResponse.success("주소가 변경되었습니다.");
    }

    /**
     * 이메일 중복 확인
     *
     * @param email
     * @return
     */
    @GetMapping("/customer/sign-up-email/{email}")
    public ApiResponse<?> customerCheckEmailDuplication(@PathVariable String email) {
        customerService.checkEmailDuplication(email);
        return ApiResponse.success("사용 가능한 이메일 입니다.");
    }

    /**
     * 닉네임 중복 확인
     *
     * @param nickname
     * @return
     */
    @GetMapping("/customer/sign-up-nickname/{nickname}")
    public ApiResponse<?> customerCheckNicknameDuplication(@PathVariable String nickname) {
        customerService.checkNicknameDuplication(nickname);
        return ApiResponse.success("사용 가능한 닉네임 입니다.");
    }
}