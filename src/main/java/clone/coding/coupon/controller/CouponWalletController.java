package clone.coding.coupon.controller;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.dto.couponwallet.CouponWalletFindAllResponse;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CouponWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/couponwallet")
@RequiredArgsConstructor
public class CouponWalletController {

    private final CouponWalletService couponWalletService;


    /**
     * 쿠폰 발급 (클릭)
     *
     * @param userDetails
     * @param couponId
     * @return
     */
    @PostMapping("/issuance/{couponId}")
    public ApiResponse<Object> couponWalletAdd(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long couponId) {
        couponWalletService.addCouponWallet(userDetails.getUsername(), couponId);
        return ApiResponse.success("쿠폰이 발급되었습니다.");
    }

    /**
     * 쿠폰 코드 생성 (쿠폰코드 등록방식)
     *
     * @param couponSaveRequest
     * @param bindingResult
     * @param couponQuantity
     * @return
     */
    @PostMapping("/couponcode/create")
    public ApiResponse<Object> couponWalletCodeAdd(@Valid @RequestBody CouponSaveRequest couponSaveRequest,
                                                   BindingResult bindingResult,
                                                   @RequestParam Long couponQuantity) {
        couponWalletService.addCouponCode(couponSaveRequest, couponQuantity);
        return ApiResponse.success("쿠폰코드가 발급되었습니다.");
    }

    /**
     * 쿠폰 코드 업데이트(고객 입력)
     *
     * @param userDetails
     * @param couponCode
     * @return
     */
    @PatchMapping("/couponcode/registration")
    public ApiResponse<Object> couponWalletCodeModify(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String couponCode) {
        couponWalletService.modifyCouponCode(userDetails.getUsername(), couponCode);
        return ApiResponse.success("쿠폰이 등록되었습니다.");
    }

    /**
     * 쿠폰 리스트 조회
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<List<CouponWalletFindAllResponse>> couponWalletList(@AuthenticationPrincipal UserDetails userDetails) {
        List<CouponWalletFindAllResponse> couponWallet = couponWalletService.findCouponWallet(userDetails.getUsername());
        return ApiResponse.success(couponWallet);
    }
}