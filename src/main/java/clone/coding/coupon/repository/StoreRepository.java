package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreNameContaining(String storeName);
}
