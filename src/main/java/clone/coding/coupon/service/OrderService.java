package clone.coding.coupon.service;

import clone.coding.coupon.dto.order.OrderFindAllByStoreResponse;
import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.dto.order.OrderMenuAndCouponFindAllResponse;
import clone.coding.coupon.dto.order.OrderSaveRequest;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.coupon.TimePolicy;
import clone.coding.coupon.entity.customer.*;
import clone.coding.coupon.entity.store.Store;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.global.exception.error.ErrorCode;
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
import static clone.coding.coupon.entity.customer.StatusType.*;
import static clone.coding.coupon.global.exception.error.ErrorCode.*;

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

        Customer customer = findCustomerByEmail(email);
        List<OrderMenu> orderMenus = findOrderMenus(customer);

        CouponWallet myCoupon = couponWalletRepository.findById(orderSaveRequest.getCouponWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_COUPON_NOT_FOUND));

        if (myCoupon.getCoupon().getDiscountType().equals(FIXED_DISCOUNT)) {
            discountAmount = myCoupon.getCoupon().getAmount();
            totalPrice = orderSaveRequest.getTotalAmount() - discountAmount;
        } else {
            discountAmount = orderSaveRequest.getTotalAmount() * myCoupon.getCoupon().getAmount() / 100;
            totalPrice = orderSaveRequest.getTotalAmount() - discountAmount;
        }

        LocalDateTime couponRedemptionAndOrderTime = LocalDateTime.now().withNano(0);
        myCoupon.couponUseProcess(couponRedemptionAndOrderTime);

        OrderMenu orderMenuInfo = orderMenus.stream().findFirst().get();
        Store store = orderMenuInfo.getMenu().getStore();

        Order order = Order.builder()
                .paymentType(orderSaveRequest.getPaymentType())
                .totalAmount(totalPrice)
                .discount(discountAmount)
                .statusType(PREPARING)
                .orderTime(couponRedemptionAndOrderTime)
                .usedCouponName(myCoupon.getCoupon().getName())
                .promotionCode(myCoupon.getCoupon().getPromotionCode())
                .customer(customer)
                .store(store)
                .build();

        for (OrderMenu orderMenu : orderMenus) {
            orderMenu.orderStatusToOrder();
            order.addOrderMenus(orderMenu);
        }

        orderRepository.save(order);
    }

    @Transactional
    public void addOrderNotCoupon(OrderSaveRequest orderSaveRequest, String email) {
        Customer customer = findCustomerByEmail(email);
        List<OrderMenu> orderMenus = findOrderMenus(customer);

        OrderMenu orderMenuInfo = orderMenus.stream().findFirst().get();
        Store store = orderMenuInfo.getMenu().getStore();

        Order order = Order.builder()
                .paymentType(orderSaveRequest.getPaymentType())
                .totalAmount(orderSaveRequest.getTotalAmount())
                .discount(0)
                .statusType(PREPARING)
                .orderTime(LocalDateTime.now().withNano(0))
                .customer(customer)
                .store(store)
                .build();

        for (OrderMenu orderMenu : orderMenus) {
            orderMenu.orderStatusToOrder();
            order.addOrderMenus(orderMenu);
        }

        orderRepository.save(order);
    }

    public OrderMenuAndCouponFindAllResponse listPurchaseOrder(String email) {
        Customer customer = findCustomerByEmail(email);
        List<OrderMenu> orderMenus = findOrderMenus(customer);

        int totalPrice = orderMenus.stream()
                .mapToInt(i -> i.getMenuCnt() * i.getMenuPrice())
                .sum();

        List<CouponWallet> usableCoupons = customer.getCouponWallets().stream()
                .filter(couponWallet -> orderMenus.stream()
                        .anyMatch(orderMenu -> isCouponUsable(couponWallet, orderMenu, totalPrice)))
                .collect(Collectors.toList());

        return new OrderMenuAndCouponFindAllResponse(orderMenus, usableCoupons, totalPrice);
    }

    public List<OrderListFindAllResponse> listOrder(String email) {
        Customer customer = findCustomerByEmail(email);
        return orderRepository.customerOrderList(customer.getId()).stream()
                .map(OrderListFindAllResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderFindAllByStoreResponse> listStoreOrder(Long storeId) {
        return orderRepository.findRecentOrdersByStore(storeId).stream()
                .map(OrderFindAllByStoreResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyOrderStatusToCooking(Long orderId, Long arrivalExpectTime) {
        Order order = findOrder(orderId);

        if (order.getStatusType() == COOKING || order.getStatusType() == DELIVERING
                || order.getStatusType() == DELIVERED || order.getStatusType() == CANCEL) {
            throw new ResourceNotFoundException(ERROR_CANNOT_CHANGE_STATUS);
        }
        order.orderStatusChangeToCooking(arrivalExpectTime);
    }

    @Transactional
    public void modifyOrderStatusToDelivering(Long orderId) {
        Order order = findOrder(orderId);

        if (order.getStatusType() == DELIVERING || order.getStatusType() == DELIVERED
                || order.getStatusType() == CANCEL) {
            throw new ResourceNotFoundException(ERROR_CANNOT_CHANGE_STATUS);
        }
        order.orderStatusChangeToDelivering();
    }

    @Transactional
    public void modifyOrderStatusToDelivered(Long orderId) {
        Order order = findOrder(orderId);

        if (order.getStatusType() == DELIVERED || order.getStatusType() == CANCEL) {
            throw new ResourceNotFoundException(ERROR_CANNOT_CHANGE_STATUS);
        }
        order.orderStatusChangeToDelivered();
    }

    @Transactional
    public void modifyOrderStatusToCancel(Long orderId) {
        Order order = findOrder(orderId);
        cancelCheck(order);
    }

    @Transactional
    public void modifyOrderStatusToCustomerCancel(Long orderId, String email) {
        Customer customer = findCustomerByEmail(email);

        Order order = orderRepository.findByIdAndCustomerId(orderId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_ORDER_NOT_FOUND));
        cancelCheck(order);
    }

    private void cancelCheck(Order order) {
        StatusType statusType = order.getStatusType();

        if (statusType == COOKING || statusType == DELIVERING || statusType == DELIVERED) {
            throw new ResourceNotFoundException(ERROR_ORDER_CANNOT_CANCELLED);
        } else if (statusType == CANCEL) {
            throw new ResourceNotFoundException(ERROR_ORDER_ALREADY_CANCELLED);
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
        Long omBrandId = orderMenu.getMenu().getStore().getBrand() != null
                ? orderMenu.getMenu().getStore().getBrand().getId()
                : null;
//        Long omBrandId = orderMenu.getMenu().getStore().getBrand().getId();
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

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MEMBER_NOT_FOUND));
    }

    private List<OrderMenu> findOrderMenus(Customer customer) {
        List<OrderMenu> orderMenus = orderMenuRepository.customerOrderMenuList(customer.getId(), OrderStatus.NOT_ORDER);
        if (orderMenus.isEmpty()) throw new ResourceNotFoundException(ERROR_ORDER_MENU_EMPTY);
        return orderMenus;
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_ORDER_NOT_FOUND));
    }
}