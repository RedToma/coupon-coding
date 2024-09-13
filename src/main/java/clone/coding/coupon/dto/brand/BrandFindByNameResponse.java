package clone.coding.coupon.dto.brand;

import clone.coding.coupon.entity.store.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandFindByNameResponse {

    private Long id;

    private String brand;

    public BrandFindByNameResponse(Brand brands) {
        this.id = brands.getId();
        this.brand = brands.getBrandName();
    }

}
