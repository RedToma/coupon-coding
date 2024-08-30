package clone.coding.coupon.entity.refresh;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refresh_id", updatable = false)
    private Long id;

    private String email;

    private String refresh;

    private String expiration;

    public Refresh(String email, String refresh, String expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
