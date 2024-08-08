package clone.coding.coupon.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
    private Long id;
    private String name;
    private String age;

    public ResponseDto(Long id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
