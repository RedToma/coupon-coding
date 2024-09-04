package clone.coding.coupon.controller;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/admin/issuer/{adminId}")
    public ApiResponse<Object> couponAdminAdd(@Valid @RequestBody CouponSaveRequest couponSaveRequest,
                                              BindingResult bindingResult,
                                              @PathVariable Long adminId) {
        couponService.addAdminCoupon(couponSaveRequest, adminId);
        return ApiResponse.success("쿠폰이 정상적으로 발급되었습니다.");
    }

}