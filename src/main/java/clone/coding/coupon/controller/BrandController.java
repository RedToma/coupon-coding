package clone.coding.coupon.controller;

import clone.coding.coupon.dto.brand.BrandFindByNameResponse;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * 브랜드 추가
     *
     * @param brandName
     * @return
     */
    @PostMapping("/new-brand")
    public ApiResponse<Object> brandAdd(@RequestParam String brandName) {
        brandService.addBrand(brandName);
        return ApiResponse.success("브랜드가 생성되었습니다.");
    }

    /**
     * 브랜드 검색
     *
     * @param brandName
     * @return
     */
    @GetMapping("/search-brand")
    public ApiResponse<List<BrandFindByNameResponse>> brandList(@RequestParam String brandName) {
        List<BrandFindByNameResponse> brands = brandService.findBrands(brandName);
        return ApiResponse.success(brands);
    }

    /**
     * 브랜드 이름 변경
     *
     * @param newBrandName
     * @param brandId
     * @return
     */
    @PatchMapping("/rename-brand/{brandId}")
    public ApiResponse<Object> brandNameModify(@RequestParam String newBrandName, @PathVariable Long brandId) {
        brandService.modifyBrandName(newBrandName, brandId);
        return ApiResponse.success("브랜드 이름을 변경했습니다.");
    }

    /**
     * 브랜드 폐업
     *
     * @param brandId
     * @return
     */
    @PatchMapping("/close-brand/{brandId}")
    public ApiResponse<Object> brandClose(@PathVariable Long brandId) {
        brandService.closeBrand(brandId);
        return ApiResponse.success("브랜드가 폐업되었습니다.");
    }
}
