package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.customer.OrderMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderMenuAndCouponFindAllResponse {
    // 주문 목록, 쿠폰 목록

    private List<OrderFindByOrderMenuResponse> orderMenu;

    private List<OrderFindByCouponResponse> coupon;

    private int totalAmount;



    public OrderMenuAndCouponFindAllResponse(List<OrderMenu> orderMenus, List<CouponWallet> couponWallets, int totalAmount) {
        this.orderMenu = orderMenus.stream()
                .map(OrderFindByOrderMenuResponse::new)
                .collect(Collectors.toList());
        this.coupon = couponWallets.stream()
                .map(OrderFindByCouponResponse::new)
                .collect(Collectors.toList());
        this.totalAmount = totalAmount;
    }
}
