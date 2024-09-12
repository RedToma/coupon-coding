package clone.coding.coupon.repository;

import clone.coding.coupon.entity.orderhistory.DeletedMemberOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedMemberOrdersRepository extends JpaRepository<DeletedMemberOrders, Long> {
}
