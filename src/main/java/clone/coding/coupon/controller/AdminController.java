package clone.coding.coupon.controller;

import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 어드민 생성(배민 관리자만 생성 가능)
     * @param type
     * @return
     */
    @PostMapping("/new")
    public ApiResponse<Object> adminAdd(@RequestParam String type) {
        adminService.addAdmin(type);
        return ApiResponse.success("관리자가 생성 되었습니다.");
    }

    /**
     * 어드민 삭제
     * @param adminId
     * @return
     */
    @DeleteMapping("/remove/{adminId}")
    public ApiResponse<Object> adminRemove(@PathVariable Long adminId) {
        adminService.removeAdmin(adminId);
        return ApiResponse.success("관리자가 삭제 되었습니다.");
    }
}
