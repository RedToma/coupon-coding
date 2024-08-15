package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
