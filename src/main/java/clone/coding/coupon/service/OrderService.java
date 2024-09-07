package clone.coding.coupon.service;

import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.dto.order.OrderMenuAndCouponFindAllResponse;
import clone.coding.coupon.dto.order.OrderSaveRequest;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.coupon.TimePolicy;
import clone.coding.coupon.entity.customer.*;
import clone.coding.coupon.repository.CouponWalletRepository;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import clone.coding.coupon.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.entity.coupon.DiscountType.FIXED_DISCOUNT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final CouponWalletRepository couponWalletRepository;

    @Transactional
    public void addOrder(OrderSaveRequest orderSaveRequest, String email) {
        int totalPrice = 0;
        int discountAmount = 0;
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<OrderMenu> orderMenus = orderMenuRepository.customerOrderMenuList(customer.getId(), OrderStatus.NOT_ORDER);
        if (orderMenus.isEmpty()) throw new IllegalArgumentException("장바구니가 비어있습니다 주문할 수 없습니다.");

        orderMenus.stream()
                .peek(orderMenu -> {
                    if (orderMenu.getMenuCnt() <= 0) throw new IllegalArgumentException("주문 오류 [주문을 생성할 수 없습니다.]");
                    orderMenu.orderStatusToOrder();
                }); //확인

        CouponWallet myCoupon = couponWalletRepository.findById(orderSaveRequest.getCouponWalletId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (myCoupon.getCoupon().getDiscountType().equals(FIXED_DISCOUNT)) {
            discountAmount = myCoupon.getCoupon().getAmount();
            totalPrice = orderSaveRequest.getTotalAmount() - discountAmount;
        } else {
            discountAmount = orderSaveRequest.getTotalAmount() * myCoupon.getCoupon().getAmount() / 100;
            totalPrice = orderSaveRequest.getTotalAmount() - discountAmount;
        }

        myCoupon.couponUseProcess();

        // 여기에다 총 금액, 할인금액 계산하기
        // 쿠폰id 번호 넘겨받고 해당 쿠폰 사용처리하기

        Order order = Order.builder()
                .paymentType(orderSaveRequest.getPaymentType())
                .totalAmount(totalPrice)
                .discount(discountAmount)
                .statusType(StatusType.PREPARING)
                .orderTime(LocalDateTime.now().withNano(0))
                .customer(customer)
                .build();

        for (OrderMenu orderMenu : orderMenus) order.addOrderMenus(orderMenu);

        orderRepository.save(order);

    }

    public OrderMenuAndCouponFindAllResponse listPurchaseOrder(String email) { // 내가 주문할 음식 목록과, 사용 가능한 쿠폰 목록 보여주기
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<OrderMenu> orderMenus = orderMenuRepository.customerOrderMenuList(customer.getId(), OrderStatus.NOT_ORDER);
        if (orderMenus.isEmpty()) throw new IllegalArgumentException("장바구니가 비어있습니다 주문할 수 없습니다.");

        int totalPrice = orderMenus.stream()
                .mapToInt(i -> i.getMenuCnt() * i.getMenuPrice())
                .sum(); //확인

        List<CouponWallet> usableCoupons = customer.getCouponWallets().stream()
                .filter(couponWallet -> orderMenus.stream()
                        .anyMatch(orderMenu -> isCouponUsable(couponWallet, orderMenu, totalPrice)))
                .collect(Collectors.toList());

        return new OrderMenuAndCouponFindAllResponse(orderMenus, usableCoupons, totalPrice);
    }

    public List<OrderListFindAllResponse> listOrder(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return orderRepository.customerOrderList(customer.getId()).stream()
                .map(OrderListFindAllResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyOrderStatusToCooking(Long orderId, Long arrivalExpectTime) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        if (order.getStatusType() == StatusType.COOKING || order.getStatusType() == StatusType.DELIVERING
                || order.getStatusType() == StatusType.DELIVERED || order.getStatusType() == StatusType.CANCEL) {
            throw new IllegalArgumentException("상태를 변경할 수 없습니다.");
        }
        order.orderStatusChangeToCooking(arrivalExpectTime);
    }

    @Transactional
    public void modifyOrderStatusToDelivering(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        if (order.getStatusType() == StatusType.DELIVERING || order.getStatusType() == StatusType.DELIVERED
                || order.getStatusType() == StatusType.CANCEL) {
            throw new IllegalArgumentException("상태를 변경할 수 없습니다.");
        }
        order.orderStatusChangeToDelivering();
    }

    @Transactional
    public void modifyOrderStatusToDelivered(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        if (order.getStatusType() == StatusType.DELIVERED || order.getStatusType() == StatusType.CANCEL) {
            throw new IllegalArgumentException("상태를 변경할 수 없습니다.");
        }
        order.orderStatusChangeToDelivered();
    }

    @Transactional
    public void modifyOrderStatusToCancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        cancelCheck(order);
    }

    @Transactional
    public void modifyOrderStatusToCustomerCancel(Long orderId, String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Order order = orderRepository.findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        cancelCheck(order);
    }

    private void cancelCheck(Order order) {
        if (order.getStatusType() == StatusType.COOKING || order.getStatusType() == StatusType.DELIVERING || order.getStatusType() == StatusType.DELIVERED) {
            throw new IllegalArgumentException("주문을 취소할 수 없습니다.");
        } else if (order.getStatusType() == StatusType.CANCEL) {
            throw new IllegalArgumentException("이미 취소된 주문 입니다.");
        }
        order.orderStatusChangeToCancel();
    }

    private boolean isCouponUsable(CouponWallet couponWallet, OrderMenu orderMenu, int totalPrice) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = LocalTime.now();
        TimePolicy timePolicy = couponWallet.getCoupon().getTimePolicy();
        Long brandId = couponWallet.getCoupon().getBrandId();
        Long storeId = couponWallet.getCoupon().getStoreId();
        Long omStoreId = orderMenu.getMenu().getStore().getId();
        Long omBrandId = orderMenu.getMenu().getStore().getBrand().getId();
        Long minOrderPrice = couponWallet.getCoupon().getMinOrderPrice();

        if (!couponWallet.isUseYn()
                && (now.isAfter(couponWallet.getStartAt()) && now.isBefore(couponWallet.getExpiredAt()))
                && (nowTime.isAfter(timePolicy.getStartTime()) && nowTime.isBefore(timePolicy.getEndTime()))) {
            if (minOrderPrice <= totalPrice) {
                if (brandId == null && storeId == null) return true;
                else if (brandId == omBrandId && storeId == null) return true;
                else if (storeId == omStoreId && brandId == null) return true;
                else if (brandId == omBrandId && storeId == omStoreId) return true;
            }
        }

        return false;
    }

    private int totalPrice(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .peek(orderMenu -> {
                    if (orderMenu.getMenuCnt() <= 0) throw new IllegalArgumentException("주문 오류 [주문을 생성할 수 없습니다.]");
                    orderMenu.orderStatusToOrder();
                })
                .mapToInt(i -> i.getMenuCnt() * i.getMenuPrice())
                .sum();
    }
}