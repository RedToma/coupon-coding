package clone.coding.coupon.service;

import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.OrderMenu;
import clone.coding.coupon.entity.customer.OrderStatus;
import clone.coding.coupon.entity.store.Menu;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.MenuRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderMenuService {

    private final OrderMenuRepository orderMenuRepository;
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void addOrderMenu(OrderMenuSaveRequest orderMenuSaveRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(orderMenuSaveRequest.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        OrderMenu orderMenu = OrderMenu.builder()
                .menu(menu)
                .menuCnt(orderMenuSaveRequest.getMenuCnt())
                .menuPrice(menu.getPrice())
                .orderStatus(OrderStatus.NOT_ORDER)
                .customer(customer)
                .build();
        orderMenuRepository.save(orderMenu);
    }

    public List<OrderMenuFindAllResponse> findOrderMenu(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

        return orderMenuRepository.customerOrderMenuList(customerId, OrderStatus.NOT_ORDER).stream()
                .map(OrderMenuFindAllResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyOrderMenu(int menuCnt, Long orderMenuId) {
        OrderMenu orderMenu = orderMenuRepository.findById(orderMenuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 목록입니다."));
        orderMenu.changeMenuCnt(menuCnt);
    }

    @Transactional
    public void removeOrderMenu(Long orderMenuId) {
        OrderMenu orderMenu = orderMenuRepository.findById(orderMenuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 목록입니다."));
        orderMenuRepository.delete(orderMenu);
    }
}
