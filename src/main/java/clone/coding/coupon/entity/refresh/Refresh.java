package clone.coding.coupon.entity.refresh;

import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refresh_id", updatable = false)
    private Long id;

    private String email;

    private String refresh;

    private LocalDateTime expiration;

    public Refresh(String email, String refresh, LocalDateTime expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
