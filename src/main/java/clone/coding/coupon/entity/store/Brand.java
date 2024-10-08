package clone.coding.coupon.entity.store;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "brand_id", updatable = false)
    private Long id;

    private String brandName;

    private boolean operatingStatus;

    public Brand(String brandName) {
        this.brandName = brandName;
        this.operatingStatus = true;
    }

    public void changeBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void changeOperatingStatus() {
        this.operatingStatus = false;
    }
}
