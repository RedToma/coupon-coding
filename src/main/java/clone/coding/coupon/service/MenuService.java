package clone.coding.coupon.service;

import clone.coding.coupon.dto.menu.MenuFindAllResponse;
import clone.coding.coupon.dto.menu.MenuSaveAndUpdateRequest;
import clone.coding.coupon.entity.store.Menu;
import clone.coding.coupon.entity.store.Store;
import clone.coding.coupon.global.exception.ErrorMessage;
import clone.coding.coupon.repository.MenuRepository;
import clone.coding.coupon.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_MENU_NOT_FOUND;
import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_STORE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void addMenu(MenuSaveAndUpdateRequest menuSaveAndUpdateRequest, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_STORE_NOT_FOUND));

        Menu menu = Menu.builder()
                .menuName(menuSaveAndUpdateRequest.getMenuName())
                .price(menuSaveAndUpdateRequest.getPrice())
                .soldout(menuSaveAndUpdateRequest.isSoldout())
                .store(store)
                .build();
        menuRepository.save(menu);
    }

    public List<MenuFindAllResponse> findAllMenu(Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_STORE_NOT_FOUND));

        return menuRepository.findByStoreId(storeId).stream()
                .map(MenuFindAllResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyMenu(MenuSaveAndUpdateRequest menuSaveAndUpdateRequest, Long menuId) {
        Menu menu = findMenu(menuId);
        menu.updateMenu(menuSaveAndUpdateRequest);
    }

    @Transactional
    public void removeMenu(Long menuId) {
        Menu menu = findMenu(menuId);
        menuRepository.delete(menu);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MENU_NOT_FOUND));
    }
}
