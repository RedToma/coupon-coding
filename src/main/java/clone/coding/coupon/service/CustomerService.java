package clone.coding.coupon.service;

import clone.coding.coupon.dto.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public boolean checkEmailDuplication(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return customerRepository.existsByNickname(nickname);
    }

    @Transactional
    public void modifyCustomerPw(CustomerPwUpdateRequest customerPwUpdateRequest, Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        customer.changePw(encoder.encode(customerPwUpdateRequest.getPassword()));
    }
}