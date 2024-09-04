package clone.coding.coupon.entity.coupon;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimePolicy {

    private boolean policyStatus;

    private LocalTime startTime;

    private LocalTime endTime;

    public TimePolicy(boolean policyStatus, LocalTime startTime, LocalTime endTime) {
        this.policyStatus = policyStatus;
        this.startTime = startTime;
        this.endTime = endTime;
    }


}
