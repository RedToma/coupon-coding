package clone.coding.coupon.service;

import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.entity.customer.*;
import clone.coding.coupon.global.jwt.JWTExtraction;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import clone.coding.coupon.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final JWTExtraction jwtExtraction;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public void addOrder(PaymentType paymentType, HttpServletRequest request) {
        Customer customer = jwtExtraction.extractCustomer(request);

        List<OrderMenu> orderMenus = orderMenuRepository.customerOrderMenuList(customer.getId(), OrderStatus.NOT_ORDER);

        if (orderMenus.isEmpty()) throw new IllegalArgumentException("장바구니가 비어있습니다 주문할 수 없습니다.");

        int totalPrice = orderMenus.stream()
                .peek(orderMenu -> {
                    if(orderMenu.getMenuCnt() <= 0) throw new IllegalArgumentException("주문 오류 [주문을 생성할 수 없습니다.]");
                    orderMenu.orderStatusToOrder();
                })
                .mapToInt(i -> i.getMenuCnt() * i.getMenuPrice())
                .sum();

        Order order = Order.builder()
                .paymentType(paymentType)
                .totalAmount(totalPrice)
                .discount(0)
                .statusType(StatusType.PREPARING)
                .orderTime(LocalDateTime.now().withNano(0))
                .customer(customer)
                .build();

        for (OrderMenu orderMenu : orderMenus) order.addOrderMenus(orderMenu);

        orderRepository.save(order);

    }

    public List<OrderListFindAllResponse> listOrder(HttpServletRequest request) {
        Customer customer = jwtExtraction.extractCustomer(request);

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
    public void modifyOrderStatusToCustomerCancel(Long orderId, HttpServletRequest request) {
        Customer customer = jwtExtraction.extractCustomer(request);

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
}