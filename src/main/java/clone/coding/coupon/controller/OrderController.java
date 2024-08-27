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

    /**
     * 주문 목록 조회
     * @param customerId
     * @return
     */
    @GetMapping("/list/{customerId}")
    public ApiResponse<List<OrderListFindAllResponse>> orderList(@PathVariable Long customerId) {
        List<OrderListFindAllResponse> orderListFindAllResponses = orderService.listOrder(customerId);
        return ApiResponse.success(orderListFindAllResponses);
    }

    /**
     * 주문 상태 변경(조리 중)
     * @param orderId
     * @param arrivalExpectTime
     * @return
     */
    @PatchMapping("/cooking-update/{orderId}")
    public ApiResponse<Object> orderStatusToCookingModify(@PathVariable Long orderId, @RequestParam Long arrivalExpectTime) {
        orderService.modifyOrderStatusToCooking(orderId, arrivalExpectTime);
        return ApiResponse.success("주문 상태가 조리 중으로 변경 되었습니다.");
    }

    /**
     * 주문 상태 변경(배달 중)
     * @param orderId
     * @return
     */
    @PatchMapping("/delivering-update/{orderId}")
    public ApiResponse<Object> orderStatusToDeliveringModify(@PathVariable Long orderId) {
        orderService.modifyOrderStatusToDelivering(orderId);
        return ApiResponse.success("주문 상태가 배달 중으로 변경 되었습니다.");
    }

    /**
     * 주문 상태 변경(배달 완료)
     * @param orderId
     * @return
     */
    @PatchMapping("/delivered-update/{orderId}")
    public ApiResponse<Object> orderStatusToDeliveredModify(@PathVariable Long orderId) {
        orderService.modifyOrderStatusToDelivered(orderId);
        return ApiResponse.success("배달이 완료 되었습니다.");
    }

    /**
     * 주문 취소(가게)
     * @param orderId
     * @return
     */
    @PatchMapping("/cancel-update/{orderId}")
    public ApiResponse<Object> orderStatusToCancelModify(@PathVariable Long orderId) { //이미 주문상태가 취소되어있으면 못하게
        orderService.modifyOrderStatusToCancel(orderId);
        return ApiResponse.success("가게 사정으로 인해 주문이 취소 되었습니다.");
    }

    /**
     * 주문 취소(고객)
     * @param orderId
     * @param customerId
     * @return
     */
    @PatchMapping("/customer-cancel-update/{orderId}/{customerId}")
    public ApiResponse<Object> orderStatusToCustomerCancelModify(@PathVariable Long orderId, @PathVariable Long customerId) {
        orderService.modifyOrderStatusToCustomerCancel(orderId, customerId);
        return ApiResponse.success("주문이 취소 되었습니다.");
    }
}
