package clone.coding.coupon.service;

import clone.coding.coupon.dto.store.StoreFindByNameResponse;
import clone.coding.coupon.dto.store.StoreSaveAndUpdateRequest;
import clone.coding.coupon.entity.admin.Admin;
import clone.coding.coupon.entity.coupon.Coupon;
import clone.coding.coupon.entity.coupon.CouponWallet;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.entity.store.Store;
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
public class StoreService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;
    private final CouponRepository couponRepository;
    private final CouponWalletRepository couponWalletRepository;

    @Transactional
    public void addStore(StoreSaveAndUpdateRequest storeSaveRequest, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_BRAND_NOT_FOUND));

        Store store = Store.builder()
                .storeName(storeSaveRequest.getStoreName())
                .storeNum(storeSaveRequest.getStoreNum())
                .address(storeSaveRequest.getAddress())
                .operatingStatus(true)
                .brand(brand)
                .build();
        storeRepository.save(store);
    }

    public List<StoreFindByNameResponse> findStore(String storeName) {
        return storeRepository.findByStoreNameContaining(storeName).stream()
                .map(StoreFindByNameResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyStore(StoreSaveAndUpdateRequest storeUpdateRequest, Long storeId) {
        Store store = findStore(storeId);
        store.changeStoreInfo(storeUpdateRequest);
    }

    @Transactional
    public void closeStore(Long storeId) {
        menuRepository.setSoldoutChangeByStoreId(storeId);
        closeStoreBusiness(storeId);
        findStore(storeId).changeOperatingStatus();
    }

    private void closeStoreBusiness(Long storeId) {
        List<Coupon> couponsByStoreId = couponRepository.findCouponsByStoreId(storeId);
        List<Long> couponIds = couponsByStoreId.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        couponsByStoreId.forEach(Coupon::disableCouponUsage);
        couponWalletRepository.findByCouponIds(couponIds).stream()
                .forEach(CouponWallet::disableCouponUsage);
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_STORE_NOT_FOUND));
    }
}
