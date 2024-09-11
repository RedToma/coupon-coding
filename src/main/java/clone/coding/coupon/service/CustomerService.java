package clone.coding.coupon.service;

import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.global.exception.error.ErrorCode;
import clone.coding.coupon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static clone.coding.coupon.global.exception.error.ErrorCode.*;

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
                .role("ROLE_CUSTOMER")
                .build();

        customerRepository.save(customer);
    }

//    public void findCustomer(CustomerLoginRequest customerLoginRequest) {
//        Customer customer = customerRepository.findByEmail(customerLoginRequest.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
//
//        if(!encoder.matches(customerLoginRequest.getPassword(), customer.getPassword())) {
//            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
//        }
//    }

    @Transactional
    public void removeCustomer(String email) {
        Customer customer = findCustomerByEmail(email);
        customerRepository.delete(customer);
    }

    @Transactional
    public void modifyCustomerPw(CustomerPwUpdateRequest customerPwUpdateRequest, String email) {
        Customer customer = findCustomerByEmail(email);
        customer.changePw(encoder.encode(customerPwUpdateRequest.getPassword()));
    }

    @Transactional
    public void modifyCustomerAddress(String address, String email) {
        Customer customer = findCustomerByEmail(email);
        customer.changeAddress(address);
    }

    public void checkEmailDuplication(String email) {
        boolean emailCheck = customerRepository.existsByEmail(email);
        if (emailCheck) throw new ResourceNotFoundException(ERROR_EMAIL_DUPLICATION);
    }

    public void checkNicknameDuplication(String nickname) {
        boolean nicknameCheck = customerRepository.existsByNickname(nickname);
        if (nicknameCheck) throw new ResourceNotFoundException(ERROR_NICKNAME_DUPLICATION);
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MEMBER_NOT_FOUND));
    }
}