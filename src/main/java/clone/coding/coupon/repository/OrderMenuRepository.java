package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.OrderMenu;
import clone.coding.coupon.entity.customer.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    @Query("SELECT o FROM OrderMenu o WHERE o.customer.id = :customerId AND o.orderStatus = :orderStatus ")
    List<OrderMenu> customerOrderMenuList(@Param("customerId") Long customerId, @Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT o FROM OrderMenu o WHERE o.customer.id = :customerId AND o.id = :orderMenuId")
    Optional<OrderMenu> findOrderMenuByCustomerIdAndOrderMenuId(@Param("customerId") Long customerId, @Param("orderMenuId") Long orderMenuId);

    @Modifying
    @Query("DELETE FROM OrderMenu o WHERE o.customer.id = :customerId")
    void deleteByCustomerOrderMenu(@Param("customerId") Long customerId);
}
