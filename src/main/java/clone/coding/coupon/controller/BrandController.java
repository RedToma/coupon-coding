package clone.coding.coupon.controller;

import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/new-brand")
    public ApiResponse<Object> brandAdd(@RequestParam String brandName) {
        brandService.addBrand(brandName);
        return ApiResponse.success("브랜드가 생성되었습니다.");
    }

}
