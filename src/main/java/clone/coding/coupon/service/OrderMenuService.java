package clone.coding.coupon.service;

import clone.coding.coupon.dto.ordermenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.ordermenu.OrderMenuSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.OrderMenu;
import clone.coding.coupon.entity.customer.OrderStatus;
import clone.coding.coupon.entity.store.Menu;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.repository.MenuRepository;
import clone.coding.coupon.repository.OrderMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.error.ErrorCode.*;

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
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MENU_NOT_FOUND));

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
    public void modifyOrderMenu(String email, int menuCnt, Long orderMenuId) {
        Customer customer = findCustomerByEmail(email);
        OrderMenu orderMenu = findOrderMenu(customer, orderMenuId);
        orderMenu.changeMenuCnt(menuCnt);
    }

    @Transactional
    public void removeOrderMenu(String email, Long orderMenuId) {
        Customer customer = findCustomerByEmail(email);
        OrderMenu orderMenu = findOrderMenu(customer, orderMenuId);
        orderMenuRepository.delete(orderMenu);
    }

    private OrderMenu findOrderMenu(Customer customer, Long orderMenuId) {
        return orderMenuRepository.findOrderMenuByCustomerIdAndOrderMenuId(customer.getId(), orderMenuId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_ORDER_MENU_NOT_FOUND));
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MEMBER_NOT_FOUND));
    }
}
