package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}
