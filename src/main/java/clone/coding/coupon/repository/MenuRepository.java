package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByStoreId(Long storeId);

    @Modifying
    @Query("UPDATE Menu m SET m.soldout = TRUE WHERE m.store.id = :storeId")
    void setSoldoutChangeByStoreId(Long storeId);
}
