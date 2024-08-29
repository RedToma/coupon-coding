package clone.coding.coupon.global.jwt;

import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTExtraction {

    private final CustomerRepository customerRepository;
    private final JWTUtil jwtUtil;


    public Customer extractCustomer(HttpServletRequest request) {
        String accessToken = request.getHeader("access");
        String email = jwtUtil.getEmail(accessToken);
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
