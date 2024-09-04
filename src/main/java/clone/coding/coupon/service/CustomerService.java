package clone.coding.coupon.service;

import clone.coding.coupon.dto.customer.CustomerLoginRequest;
import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
                .role("ROLE_ADMIN")
                .build();

        customerRepository.save(customer);
    }

    public void findCustomer(CustomerLoginRequest customerLoginRequest) {
        Customer customer = customerRepository.findByEmail(customerLoginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!encoder.matches(customerLoginRequest.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void removeCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        customerRepository.delete(customer);
    }

    @Transactional
    public void modifyCustomerPw(CustomerPwUpdateRequest customerPwUpdateRequest, String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        customer.changePw(encoder.encode(customerPwUpdateRequest.getPassword()));
    }

    @Transactional
    public void modifyCustomerAddress(String address, String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        customer.changeAddress(address);
    }

    public boolean checkEmailDuplication(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return customerRepository.existsByNickname(nickname);
    }
}