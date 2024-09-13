package clone.coding.coupon.dto.store;

import clone.coding.coupon.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreFindByNameResponse {

    private Long id;

    private String storeName;

    private String address;

    public StoreFindByNameResponse(Store store) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
    }
}
