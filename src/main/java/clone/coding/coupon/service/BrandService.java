package clone.coding.coupon.service;

import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public void addBrand(String brandName) {
        Brand brand = new Brand(brandName);
        brandRepository.save(brand);
    }
}
