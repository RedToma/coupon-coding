package clone.coding.coupon.repository;

import clone.coding.coupon.entity.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByBrandId(Long brandId);
}
