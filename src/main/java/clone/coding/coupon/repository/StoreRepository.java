package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreNameContaining(String storeName);

    Optional<Store> findByStoreName(String storeName);
}
