package clone.coding.coupon;

import clone.coding.coupon.dto.ResponseDto;
import clone.coding.coupon.entity.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/restDocsTest")
    public String restDocsTestApi() {
        return "Hello World!";
    }

    @GetMapping("/test/{id}")
    public ResponseDto test(@PathVariable Long id) {
        return new ResponseDto(id, "홍길동", "22");
    }

    @Transactional
    @GetMapping("/test")
    public String test2() {
        Admin admin = new Admin("운영자");
        em.persist(admin);
        return "생성완료";
    }
}
