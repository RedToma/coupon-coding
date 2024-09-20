package clone.coding.coupon.entity.customer;

import clone.coding.coupon.entity.BaseTimeEntity;
import clone.coding.coupon.entity.coupon.CouponWallet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id", updatable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String address;

    @Column(unique = true)
    private String phoneNum;

    private String role;

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<CouponWallet> couponWallets = new ArrayList<>();

    public void addCouponWallets(CouponWallet couponWallet) {
        couponWallets.add(couponWallet);
        couponWallet.setCustomer(this);
    }

    public void changePw(String password) {
        this.password = password;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public Customer(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Customer(String email) {
        this.email = email;
    }
}
