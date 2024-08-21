package clone.coding.coupon.repository;

import clone.coding.coupon.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Customer> findByEmail(String email);

}
