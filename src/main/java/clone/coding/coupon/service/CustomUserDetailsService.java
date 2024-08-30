package clone.coding.coupon.service;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email).get();

        if (customer != null) {
            return new CustomUserDetails(customer);
        }



        return null;
    }
}
