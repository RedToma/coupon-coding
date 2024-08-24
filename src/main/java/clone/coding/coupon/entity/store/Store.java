package clone.coding.coupon.entity.store;

import clone.coding.coupon.dto.store.StoreSaveAndUpdateRequest;
import clone.coding.coupon.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id", updatable = false)
    private Long id;

    @Column(unique = true)
    private String storeName;

    @Column(unique = true)
    private String storeNum;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void changeStoreInfo(StoreSaveAndUpdateRequest storeSaveAndUpdateRequest) {
        storeName = storeSaveAndUpdateRequest.getStoreName();
        storeNum = storeSaveAndUpdateRequest.getStoreNum();
        address = storeSaveAndUpdateRequest.getAddress();
    }
}
