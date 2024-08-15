package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
