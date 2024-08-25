package clone.coding.coupon.controller;

import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.entity.customer.PaymentType;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     * @param paymentType
     * @param customerId
     * @return
     */
    @PostMapping("/new/{customerId}")
    public ApiResponse<Object> orderAdd(@RequestParam PaymentType paymentType, @PathVariable Long customerId) {
        orderService.addOrder(paymentType, customerId);
        return ApiResponse.success("주문이 생성되었습니다.");
    }

    @GetMapping("/list/{customerId}")
    public ApiResponse<List<OrderListFindAllResponse>> orderList(@PathVariable Long customerId) {
        List<OrderListFindAllResponse> orderListFindAllResponses = orderService.listOrder(customerId);
        return ApiResponse.success(orderListFindAllResponses);
    }
}
