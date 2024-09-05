package clone.coding.coupon.controller;

import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CouponWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/couponwallet")
@RequiredArgsConstructor
public class CouponWalletController {

    private final CouponWalletService couponWalletService;


    @PostMapping("/issuance/{couponId}")
    public ApiResponse<Object> couponWalletAdd(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long couponId) {
        couponWalletService.addCouponWallet(userDetails.getUsername(), couponId);
        return ApiResponse.success("쿠폰이 발급되었습니다.");
    }
}