package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomerSaveRequest;
import clone.coding.coupon.global.ApiResponse;
import clone.coding.coupon.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer/sign-up")
    public ApiResponse<Object> customerSave(@Valid @RequestBody CustomerSaveRequest customerSaveRequest, BindingResult bindingResult) {

        customerService.addCustomer(customerSaveRequest);

        return ApiResponse.success();
    }
}
