package clone.coding.coupon.service;

import clone.coding.coupon.dto.store.StoreFindByNameResponse;
import clone.coding.coupon.dto.store.StoreSaveAndUpdateRequest;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.entity.store.Store;
import clone.coding.coupon.repository.BrandRepository;
import clone.coding.coupon.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_BRAND_NOT_FOUND;
import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_STORE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public void addStore(StoreSaveAndUpdateRequest storeSaveRequest, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_BRAND_NOT_FOUND));

        Store store = Store.builder()
                .storeName(storeSaveRequest.getStoreName())
                .storeNum(storeSaveRequest.getStoreNum())
                .address(storeSaveRequest.getAddress())
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
    public void removeStore(Long storeId) {
        Store store = findStore(storeId);
        storeRepository.delete(store);
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_STORE_NOT_FOUND));
    }
}
