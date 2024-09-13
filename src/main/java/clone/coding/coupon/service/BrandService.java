package clone.coding.coupon.service;

import clone.coding.coupon.dto.brand.BrandFindByNameResponse;
import clone.coding.coupon.entity.admin.Admin;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.global.exception.ResourceNotFoundException;
import clone.coding.coupon.global.exception.error.ErrorCode;
import clone.coding.coupon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.error.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final CouponRepository couponRepository;
    private final CouponWalletRepository couponWalletRepository;

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
    public void closeBrand(Long brandId) {
        storeRepository.setBrandIdToNullByBrandId(brandId);
        Admin admin = adminRepository.findByBrandId(brandId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_ADMIN_NOT_FOUND));

        closeBrandBusiness(admin);
        findBrand(brandId).changeOperatingStatus();
        adminRepository.delete(admin);
    }

    private void closeBrandBusiness(Admin admin) {
        List<Coupon> couponsByIssuerCode = couponRepository.findCouponsByIssuerCode(admin.getId());
        List<Long> couponIds = couponsByIssuerCode.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        couponsByIssuerCode.forEach(Coupon::disableCouponUsage);
        couponWalletRepository.findByCouponIds(couponIds).stream()
                .forEach(CouponWallet::disableCouponUsage);
    }

    private Brand findBrand(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_BRAND_NOT_FOUND));
    }
}