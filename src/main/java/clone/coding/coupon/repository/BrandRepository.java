package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
