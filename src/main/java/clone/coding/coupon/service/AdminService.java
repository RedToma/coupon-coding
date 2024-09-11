package clone.coding.coupon.service;

import clone.coding.coupon.entity.admin.Admin;
import clone.coding.coupon.entity.store.Brand;
import clone.coding.coupon.repository.AdminRepository;
import clone.coding.coupon.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static clone.coding.coupon.global.exception.ErrorMessage.ERROR_ADMIN_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public void addAdmin(String type) {
        Admin admin;
        Brand brand = brandRepository.findByBrandName(type);

        if (brand == null) admin = new Admin(type + "_ADMIN", null);
        else admin = new Admin(type + "_ADMIN", brand.getId());

        adminRepository.save(admin);
    }

    @Transactional
    public void removeAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_ADMIN_NOT_FOUND));
        adminRepository.delete(admin);
    }
}
