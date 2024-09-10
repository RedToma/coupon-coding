package clone.coding.coupon.controller;

import clone.coding.coupon.dto.menu.MenuFindAllResponse;
import clone.coding.coupon.dto.menu.MenuSaveAndUpdateRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    /**
     * 메뉴 추가
     * @param menuSaveAndUpdateRequest
     * @param storeId
     * @return
     */
    @PostMapping("/new/{storeId}")
    public ApiResponse<Object> menuAdd(@Valid @RequestBody MenuSaveAndUpdateRequest menuSaveAndUpdateRequest,
                                       BindingResult bindingResult,
                                       @PathVariable Long storeId) {
        menuService.addMenu(menuSaveAndUpdateRequest, storeId);
        return ApiResponse.success("메뉴가 추가 되었습니다.");
    }

    /**
     * 메뉴 조회
     * @param storeId
     * @return
     */
    @GetMapping("/list/{storeId}")
    public ApiResponse<List<MenuFindAllResponse>> menuList(@PathVariable Long storeId) {
        List<MenuFindAllResponse> allMenu = menuService.findAllMenu(storeId);
        return ApiResponse.success(allMenu);
    }

    /**
     * 메뉴 수정
     * @param menuSaveAndUpdateRequest
     * @param bindingResult
     * @param menuId
     * @return
     */
    @PatchMapping("/update/{menuId}")
    public ApiResponse<Object> menuModify(@Valid @RequestBody MenuSaveAndUpdateRequest menuSaveAndUpdateRequest,
                                          BindingResult bindingResult,
                                          @PathVariable Long menuId) {
        menuService.modifyMenu(menuSaveAndUpdateRequest, menuId);
        return ApiResponse.success("메뉴 정보가 업데이트 되었습니다.");
    }

    /**
     * 메뉴 삭제
     * @param menuId
     * @return
     */
    @DeleteMapping("/remove/{menuId}")
    public ApiResponse<Object> menuRemove(@PathVariable Long menuId) {
        menuService.removeMenu(menuId);
        return ApiResponse.success("메뉴가 삭제 되었습니다.");
    }
}