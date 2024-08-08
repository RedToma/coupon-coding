package clone.coding.coupon;

import clone.coding.coupon.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    @GetMapping("/restDocsTest")
    public String restDocsTestApi() {
        return "Hello World!";
    }

    @GetMapping("/test/{id}")
    public ResponseDto test(@PathVariable Long id) {
        return new ResponseDto(id, "홍길동", "22");
    }
}
