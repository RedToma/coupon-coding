package clone.coding.coupon.service;

import clone.coding.coupon.dto.brand.BrandFindByNameResponse;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<BrandFindByNameResponse> findBrands(String brandName) {
        return brandRepository.findByBrandNameContaining(brandName).stream()
                .map(BrandFindByNameResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void modifyBrandName(String newBrandName, Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
        brand.changeBrandName(newBrandName);
    }

    @Transactional
    public void removeBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
        brandRepository.delete(brand);
    }

}