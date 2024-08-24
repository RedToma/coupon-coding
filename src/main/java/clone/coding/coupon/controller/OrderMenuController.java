package clone.coding.coupon.controller;

import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.OrderMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * 장바구니 생성
     * @param orderMenuSaveRequest
     * @param bindingResult
     * @param customerId
     * @return
     */
    @PostMapping("/new/{customerId}")
    public ApiResponse<Object> orderMenuAdd(@Valid @RequestBody OrderMenuSaveRequest orderMenuSaveRequest, BindingResult bindingResult, @PathVariable Long customerId) {
        orderMenuService.addOrderMenu(orderMenuSaveRequest, customerId);
        return ApiResponse.success();
    }

    /**
     * 장바구니 조회
     * @param customerId
     * @return
     */
    @GetMapping("/list/{customerId}")
    public ApiResponse<List<OrderMenuFindAllResponse>> orderMenuList(@PathVariable Long customerId) {
        List<OrderMenuFindAllResponse> orderMenu = orderMenuService.findOrderMenu(customerId);
        return ApiResponse.success(orderMenu);
    }
}
