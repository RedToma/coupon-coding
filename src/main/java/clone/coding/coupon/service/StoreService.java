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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public void addStore(StoreSaveAndUpdateRequest storeSaveRequest, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드 입니다."));

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
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지점 입니다."));

        store.changeStoreInfo(storeUpdateRequest);
    }

    @Transactional
    public void removeStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지점 입니다."));
        storeRepository.delete(store);
    }
}
