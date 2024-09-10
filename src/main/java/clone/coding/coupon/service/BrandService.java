package clone.coding.coupon.service;

import clone.coding.coupon.dto.brand.BrandFindByNameResponse;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.global.exception.ErrorMessage;
import clone.coding.coupon.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_BRAND_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public void addBrand(String brandName) {
        brandRepository.save(new Brand(brandName));
    }

    public List<BrandFindByNameResponse> findBrands(String brandName) {
        return brandRepository.findByBrandNameContaining(brandName).stream()
                .map(BrandFindByNameResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyBrandName(String newBrandName, Long brandId) {
        Brand brand = findBrand(brandId);
        brand.changeBrandName(newBrandName);
    }

    @Transactional
    public void removeBrand(Long brandId) {
        Brand brand = findBrand(brandId);
        brandRepository.delete(brand);
    }

    private Brand findBrand(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_BRAND_NOT_FOUND));
    }
}