package clone.coding.coupon.repository;

import clone.coding.coupon.entity.store.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByBrandNameContaining(String brandName);

    Brand findByBrandName(String brandName);
}
