package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    @Query("SELECT o FROM OrderMenu o WHERE o.customer.id = :customerId AND o.order IS NULL ")
    List<OrderMenu> customerOrderMenu(@Param("customerId") Long customerId);
}
