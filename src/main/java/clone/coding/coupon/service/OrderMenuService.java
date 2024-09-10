package clone.coding.coupon.service;

import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.OrderMenu;
import clone.coding.coupon.entity.customer.OrderStatus;
import clone.coding.coupon.entity.store.Menu;
import clone.coding.coupon.global.exception.ErrorMessage;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.MenuRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_MEMBER_NOT_FOUND;
import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_ORDER_MENU_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderMenuService {

    private final OrderMenuRepository orderMenuRepository;
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void addOrderMenu(OrderMenuSaveRequest orderMenuSaveRequest, String email) {
        Customer customer = findCustomerByEmail(email);

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

    public List<OrderMenuFindAllResponse> findOrderMenu(String email) {
        Customer customer = findCustomerByEmail(email);
        return orderMenuRepository.customerOrderMenuList(customer.getId(), OrderStatus.NOT_ORDER).stream()
                .map(OrderMenuFindAllResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyOrderMenu(int menuCnt, Long orderMenuId) {
        OrderMenu orderMenu = findOrderMenu(orderMenuId);
        orderMenu.changeMenuCnt(menuCnt);
    }

    @Transactional
    public void removeOrderMenu(Long orderMenuId) {
        OrderMenu orderMenu = findOrderMenu(orderMenuId);
        orderMenuRepository.delete(orderMenu);
    }

    private OrderMenu findOrderMenu(Long orderMenuId) {
        return orderMenuRepository.findById(orderMenuId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_ORDER_MENU_NOT_FOUND));
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MEMBER_NOT_FOUND));
    }
}
