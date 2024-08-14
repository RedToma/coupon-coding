package clone.coding.coupon.entity.admin;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_id", updatable = false)
    private Long id;

    private String adminType;

    public Admin(String adminType) {
        this.adminType = adminType;
    }
}

