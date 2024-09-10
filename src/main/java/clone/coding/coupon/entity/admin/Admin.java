package clone.coding.coupon.entity.admin;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_id", updatable = false)
    private Long id;

    private String adminType;

    private Long brandId;

    public Admin(String adminType, Long brandId) {
        this.adminType = adminType;
        this.brandId = brandId;
    }
}

