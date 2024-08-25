package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.orderMenus om WHERE o.customer.id = :customerId")
    List<Order> customerOrderList(@Param("customerId") Long customerId);

}
