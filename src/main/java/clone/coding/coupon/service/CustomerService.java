package clone.coding.coupon.service;

import clone.coding.coupon.dto.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void addCustomer(CustomerSaveRequest customerSaveRequest) {

        Customer customer = Customer.builder()
                .email(customerSaveRequest.getEmail())
                .password(encoder.encode(customerSaveRequest.getPassword()))
                .nickname(customerSaveRequest.getNickname())
                .address(customerSaveRequest.getAddress())
                .phoneNum(customerSaveRequest.getPhoneNum())
                .build();

        customerRepository.save(customer);
    }
}
