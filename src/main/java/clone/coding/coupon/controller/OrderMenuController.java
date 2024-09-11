package clone.coding.coupon.controller;

import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.OrderMenuService;
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
@RequestMapping("/order-menu")
@RequiredArgsConstructor
public class OrderMenuController {

    private final OrderMenuService orderMenuService;

    /**
     * 장바구니 추가
     *
     * @param orderMenuSaveRequest
     * @param bindingResult
     * @param userDetails
     * @return
     */
    @PostMapping("/new")
    public ApiResponse<Object> orderMenuAdd(@Valid @RequestBody OrderMenuSaveRequest orderMenuSaveRequest,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        orderMenuService.addOrderMenu(orderMenuSaveRequest, userDetails.getUsername());
        return ApiResponse.success("장바구니에 추가 되었습니다.");
    }

    /**
     * 장바구니 조회
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<List<OrderMenuFindAllResponse>> orderMenuList(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderMenuFindAllResponse> orderMenu = orderMenuService.findOrderMenu(userDetails.getUsername());
        return ApiResponse.success(orderMenu);
    }

    /**
     * 장바구니 수량 변경
     *
     * @param menuCnt
     * @param orderMenuId
     * @return
     */
    @PatchMapping("/update/{orderMenuId}")
    public ApiResponse<Object> orderMenuModify(@RequestParam int menuCnt, @PathVariable Long orderMenuId) {
        orderMenuService.modifyOrderMenu(menuCnt, orderMenuId);
        return ApiResponse.success("수량이 변경 되었습니다.");
    }

    /**
     * 장바구니 선택 메뉴 삭제
     *
     * @param orderMenuId
     * @return
     */
    @DeleteMapping("/remove/{orderMenuId}")
    public ApiResponse<Object> orderMenuRemove(@PathVariable Long orderMenuId) {
        orderMenuService.removeOrderMenu(orderMenuId);
        return ApiResponse.success("선택한 메뉴가 삭제 되었습니다.");
    }
}
