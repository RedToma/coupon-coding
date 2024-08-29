package clone.coding.coupon.global.jwt;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.entity.customer.Customer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            return;
        }

        String JwtToken = authorization.split(" ")[1];

        if (jwtUtil.isExpired(JwtToken)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        String email = jwtUtil.getEmail(JwtToken);
        String role = jwtUtil.getRole(JwtToken);

        Customer customer = new Customer(email, "temppassword", role);

        CustomUserDetails customUserDetails = new CustomUserDetails(customer);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
