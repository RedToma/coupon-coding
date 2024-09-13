package clone.coding.coupon.service;

import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.Order;
import clone.coding.coupon.entity.orderhistory.DeletedMemberOrders;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.global.exception.error.ErrorCode;
import clone.coding.coupon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.error.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {

    private final PasswordEncoder encoder;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final CouponWalletRepository couponWalletRepository;
    private final DeletedMemberOrdersRepository deletedMemberOrdersRepository;

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
        processMemberWithdrawal(customer);
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

    private void processMemberWithdrawal(Customer customer) {
        couponWalletRepository.deleteByCustomerCouponWallet(customer.getId()); // 회원 발행쿠폰 삭제
        orderMenuRepository.deleteByCustomerOrderMenu(customer.getId()); // 회원 장바구니 삭제

        List<DeletedMemberOrders> deletedMemberOrders = new ArrayList<>();
        for (Order order : orderRepository.findCustomerOrderList(customer.getId())) {
            DeletedMemberOrders build = DeletedMemberOrders.builder()
                    .email(customer.getEmail())
                    .phoneNum(customer.getPhoneNum())
                    .nickName(customer.getNickname())
                    .paymentType(order.getPaymentType())
                    .totalAmount(order.getTotalAmount())
                    .discount(order.getDiscount())
                    .orderTime(order.getOrderTime())
                    .arrivalTime(order.getArrivalTime())
                    .statusType(order.getStatusType())
                    .couponName(order.getUsedCouponName())
                    .promotionCode(order.getPromotionCode())
                    .build();
            deletedMemberOrders.add(build);
        }

        deletedMemberOrdersRepository.saveAll(deletedMemberOrders);
        orderRepository.deleteByCustomerOrder(customer.getId());
        customerRepository.delete(customer);
    }
}