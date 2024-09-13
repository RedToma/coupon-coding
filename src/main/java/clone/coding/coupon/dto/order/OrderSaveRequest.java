package clone.coding.coupon.dto.order;

import clone.coding.coupon.entity.customer.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveRequest {

    private PaymentType paymentType;

    private Long couponWalletId;

    private int totalAmount;
}
