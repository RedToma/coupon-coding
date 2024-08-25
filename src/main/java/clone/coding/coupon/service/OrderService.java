package clone.coding.coupon.service;

import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.entity.customer.*;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import clone.coding.coupon.repository.OrderRepository;
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

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public void addOrder(PaymentType paymentType, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<OrderMenu> orderMenus = orderMenuRepository.customerOrderMenuList(customerId, OrderStatus.NOT_ORDER);

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
                .orderTime(LocalDateTime.now())
                .customer(customer)
                .build();

        for (OrderMenu orderMenu : orderMenus) order.addOrderMenus(orderMenu);

        orderRepository.save(order);

    }

    public List<OrderListFindAllResponse> listOrder(Long customerId) { //(총 금액, 주문상태, 주문시간) 주문 테이블, (가게이름) 매장 테이블, (주문메뉴) 메뉴 테이블
        return orderRepository.customerOrderList(customerId).stream()
                .map(OrderListFindAllResponse::new)
                .collect(Collectors.toList());
    }
}