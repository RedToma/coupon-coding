package clone.coding.coupon.controller;

import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/delivery/customer/sign-up")
    public String customerSave() {
        Customer customer = new Customer("ji98min@naver.com", "1234", "Test", "강남", "010-1234-5678");
        customerService.addCustomer(customer);

        return "success";
    }
}
