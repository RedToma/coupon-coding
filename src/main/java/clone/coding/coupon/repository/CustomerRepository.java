package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);

}
