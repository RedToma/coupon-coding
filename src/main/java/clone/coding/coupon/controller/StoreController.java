package clone.coding.coupon.controller;

import clone.coding.coupon.dto.store.StoreFindByNameResponse;
import clone.coding.coupon.dto.store.StoreSaveAndUpdateRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 지점 추가
     * @param storeSaveAndUpdateRequest
     * @param bindingResult
     * @param brandId
     * @return
     */
    @PostMapping("/new/{brandId}")
    public ApiResponse<Object> storeAdd(@Valid @RequestBody StoreSaveAndUpdateRequest storeSaveAndUpdateRequest,
                                        BindingResult bindingResult,
                                        @PathVariable Long brandId) {
        storeService.addStore(storeSaveAndUpdateRequest, brandId);
        return ApiResponse.success("지점이 추가되었습니다.");
    }

    /**
     * 지점 조회
     * @param storeName
     * @return
     */
    @GetMapping("/search")
    public ApiResponse<List<StoreFindByNameResponse>> storeDetails(@RequestParam String storeName) {
        List<StoreFindByNameResponse> store = storeService.findStore(storeName);
        return ApiResponse.success(store);
    }

    /**
     * 지점 정보 변경
     * @param storeSaveAndUpdateRequest
     * @param bindingResult
     * @param storeId
     * @return
     */
    @PatchMapping("/info/{storeId}")
    public ApiResponse<Object> storeModify(@Valid @RequestBody StoreSaveAndUpdateRequest storeSaveAndUpdateRequest,
                                           BindingResult bindingResult,
                                           @PathVariable Long storeId) {
        storeService.modifyStore(storeSaveAndUpdateRequest, storeId);
        return ApiResponse.success("지점 정보가 변경되었습니다.");
    }


    /**
     * 지점 삭제
     * @param storeId
     * @return
     */
    @DeleteMapping("/remove/{storeId}")
    public ApiResponse<Object> storeRemove(@PathVariable Long storeId) {
        storeService.removeStore(storeId);
        return ApiResponse.success("지점이 삭제되었습니다.");
    }
}
