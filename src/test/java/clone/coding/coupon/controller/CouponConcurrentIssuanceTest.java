package clone.coding.coupon.controller;

import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.TimePolicy;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.repository.CouponRepository;
import clone.coding.coupon.repository.CustomerRepository;
import clone.coding.coupon.service.CouponWalletService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static clone.coding.coupon.entity.coupon.DiscountType.FIXED_DISCOUNT;
import static clone.coding.coupon.entity.coupon.IssuerType.ALL;

@SpringBootTest
class CouponConcurrentIssuanceTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CouponWalletService couponWalletService;


    @Test
    @DisplayName("쿠폰 발급(클릭) 테스트 - 비관적 락")
    void testCouponWalletAdd() throws InterruptedException {
        //given
        int numberOfThreads = 1000;
        LocalDateTime startAt = LocalDateTime.of(2024, 9, 7, 00, 00, 00);
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 29, 23, 59, 59);
        LocalTime startTime = LocalTime.of(00, 00);
        LocalTime expiredTime = LocalTime.of(23, 59, 59);
        TimePolicy timePolicy = new TimePolicy(startTime, expiredTime);

        Coupon adminCoupon = Coupon.builder()
                .issuerCode(1L)
                .name("이벤트 쿠폰")
                .amount(3000)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .timePolicy(timePolicy)
                .minOrderPrice(10000L)
                .discountType(FIXED_DISCOUNT)
                .issuerType(ALL)
                .maxCnt(1000)
                .maxCntPerCus(1000)
                .allocatedCnt(0)
                .available(true)
                .promotionCode(UUID.randomUUID().toString())
                .build();
        Coupon coupon = couponRepository.save(adminCoupon);
        Customer customer = customerRepository.save(new Customer("test@naver.com"));


        //when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                couponWalletService.addCouponWallet(customer.getEmail(), coupon.getId());
                latch.countDown();
            });
            System.out.println(i);
        }

        //then
        latch.await();
        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        System.out.println("1000장 마감");
        Assertions.assertThat(findCoupon.getAllocatedCnt()).isEqualTo(1000);
    }
}
