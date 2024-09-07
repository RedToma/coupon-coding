package clone.coding.coupon.config;

import clone.coding.coupon.entity.admin.Admin;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.TimePolicy;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.OrderMenu;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.entity.store.Menu;
import clone.coding.coupon.entity.store.Store;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static clone.coding.coupon.entity.coupon.DiscountType.FIXED_DISCOUNT;
import static clone.coding.coupon.entity.coupon.IssuerType.ALL;
import static clone.coding.coupon.entity.coupon.IssuerType.BRAND;
import static clone.coding.coupon.entity.customer.OrderStatus.NOT_ORDER;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        initDataService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitDataService {

        private final PasswordEncoder encoder;

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            Customer customer = Customer.builder()
                    .email("ji98min@naver.com")
                    .password(encoder.encode("Password1!"))
                    .nickname("RedToMa")
                    .address("강남")
                    .phoneNum("010-7485-7330")
                    .build();
            em.persist(customer);

            Brand brand1 = new Brand("BBQ");
            em.persist(brand1);

            Brand brand2 = new Brand("BHC");
            em.persist(brand2);

            Store store1 = Store.builder()
                    .storeName("BBQ 송도1동")
                    .storeNum("070-1231-4567")
                    .address("송도")
                    .brand(brand1)
                    .build();
            em.persist(store1);

            Store store2 = Store.builder()
                    .storeName("BHC 송도1동")
                    .storeNum("070-1232-4567")
                    .address("송도")
                    .brand(brand2)
                    .build();
            em.persist(store2);

            Menu menu1 = Menu.builder()
                    .store(store1)
                    .menuName("치킨1")
                    .price(10000)
                    .soldout(false)
                    .build();
            em.persist(menu1);

            Menu menu2 = Menu.builder()
                    .store(store2)
                    .menuName("치킨2")
                    .price(20000)
                    .soldout(false)
                    .build();
            em.persist(menu2);

            Admin admin1 = new Admin("BAMIN_ADMIN", null);
            em.persist(admin1);

            Admin admin2 = new Admin("BBQ_ADMIN", 1L);
            em.persist(admin2);

            Admin admin3 = new Admin("BHC_ADMIN", 2L);
            em.persist(admin3);

            LocalDateTime startAt = LocalDateTime.of(2024, 9, 7, 00, 00, 00);
            LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 29, 23, 59, 59);
            LocalTime startTime = LocalTime.of(00, 00);
            LocalTime expiredTime = LocalTime.of(23, 59, 59);
            TimePolicy timePolicy = new TimePolicy(startTime, expiredTime);

            Coupon adminCoupon = Coupon.builder()
                    .issuerCode(1L)
                    .name("배민 이벤트 쿠폰")
                    .amount(3000)
                    .startAt(startAt)
                    .expiredAt(expiredAt)
                    .timePolicy(timePolicy)
                    .minOrderPrice(10000L)
                    .discountType(FIXED_DISCOUNT)
                    .issuerType(ALL)
                    .maxCnt(1000)
                    .maxCntPerCus(10)
                    .allocatedCnt(0)
                    .available(true)
                    .promotionCode(UUID.randomUUID().toString())
                    .build();
            em.persist(adminCoupon);

            Coupon bbqCoupon = Coupon.builder()
                    .issuerCode(2L)
                    .name("브랜드 치킨 할인쿠폰 BBQ")
                    .brandId(1L)
                    .amount(3000)
                    .startAt(startAt)
                    .expiredAt(expiredAt)
                    .timePolicy(timePolicy)
                    .minOrderPrice(10000L)
                    .discountType(FIXED_DISCOUNT)
                    .issuerType(BRAND)
                    .maxCnt(1000)
                    .maxCntPerCus(10)
                    .allocatedCnt(0)
                    .available(true)
                    .promotionCode(UUID.randomUUID().toString())
                    .build();
            em.persist(bbqCoupon);

            Coupon bhcCoupon = Coupon.builder()
                    .issuerCode(3L)
                    .name("브랜드 치킨 할인쿠폰 BHC")
                    .brandId(2L)
                    .amount(3000)
                    .startAt(startAt)
                    .expiredAt(expiredAt)
                    .timePolicy(timePolicy)
                    .minOrderPrice(10000L)
                    .discountType(FIXED_DISCOUNT)
                    .issuerType(BRAND)
                    .maxCnt(1000)
                    .maxCntPerCus(10)
                    .allocatedCnt(0)
                    .available(true)
                    .promotionCode(UUID.randomUUID().toString())
                    .build();
            em.persist(bhcCoupon);

            Coupon bbqStoreCoupon = Coupon.builder()
                    .issuerCode(2L)
                    .name("BBQ 송도1동 첫 주문쿠폰")
                    .brandId(1L)
                    .storeId(1L)
                    .amount(3000)
                    .startAt(startAt)
                    .expiredAt(expiredAt)
                    .timePolicy(timePolicy)
                    .minOrderPrice(10000L)
                    .discountType(FIXED_DISCOUNT)
                    .issuerType(BRAND)
                    .maxCnt(1000)
                    .maxCntPerCus(10)
                    .allocatedCnt(0)
                    .available(true)
                    .promotionCode(UUID.randomUUID().toString())
                    .build();
            em.persist(bbqStoreCoupon);

            Coupon bhcStoreCoupon = Coupon.builder()
                    .issuerCode(3L)
                    .name("BHC 송도1동 첫 주문쿠폰")
                    .brandId(2L)
                    .storeId(2L)
                    .amount(3000)
                    .startAt(startAt)
                    .expiredAt(expiredAt)
                    .timePolicy(timePolicy)
                    .minOrderPrice(10000L)
                    .discountType(FIXED_DISCOUNT)
                    .issuerType(BRAND)
                    .maxCnt(1000)
                    .maxCntPerCus(10)
                    .allocatedCnt(0)
                    .available(true)
                    .promotionCode(UUID.randomUUID().toString())
                    .build();
            em.persist(bhcStoreCoupon);

            OrderMenu orderMenu = OrderMenu.builder()
                    .menu(menu1)
                    .menuCnt(2)
                    .menuPrice(10000)
                    .customer(customer)
                    .orderStatus(NOT_ORDER)
                    .build();
            em.persist(orderMenu);

        }
    }
}
