package clone.coding.coupon.controller;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 어드민 쿠폰 발행
     *
     * @param couponSaveRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/admin/issuer")
    public ApiResponse<Object> couponAdminAdd(@Valid @RequestBody CouponSaveRequest couponSaveRequest, BindingResult bindingResult) {
        couponService.addAdminCoupon(couponSaveRequest);
        return ApiResponse.success("어드민 쿠폰이 정상적으로 발급되었습니다.");
    }


    /**
     * 브랜드 쿠폰 발행
     *
     * @param couponSaveRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/brand/issuer")
    public ApiResponse<Object> couponBrandAdd(@Valid @RequestBody CouponSaveRequest couponSaveRequest, BindingResult bindingResult) {
        couponService.addBrandCoupon(couponSaveRequest);
        return ApiResponse.success("브랜드 쿠폰이 정상적으로 발급되었습니다.");
    }

    /**
     * 스토어 쿠폰 발행
     *
     * @param couponSaveRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/store/issuer")
    public ApiResponse<Object> couponStoreAdd(@Valid @RequestBody CouponSaveRequest couponSaveRequest, BindingResult bindingResult) {
        couponService.addStoreCoupon(couponSaveRequest);
        return ApiResponse.success("스토어 쿠폰이 정상적으로 발급되었습니다.");
    }

}