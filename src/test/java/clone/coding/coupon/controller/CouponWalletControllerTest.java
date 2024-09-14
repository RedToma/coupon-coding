package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.dto.couponwallet.CouponWalletFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.entity.coupon.DiscountType;
import clone.coding.coupon.entity.coupon.IssuerType;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.CouponWalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponWalletControllerTest extends AbstractRestDocsTests {

    @MockBean
    private CouponWalletService couponWalletService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        Customer customer = Customer.builder()
                .id(1L)
                .email("test@email.com")
                .password("Password1!")
                .nickname("testuser")
                .address("서울")
                .phoneNum("010-1234-5678")
                .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(customer);

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));
    }


    @Test
    @DisplayName("쿠폰 발급(클릭) 테스트")
    void testCouponWalletAdd() throws Exception {
        //given
        Long couponId = 1L;

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        //when & then
        Mockito.doNothing().when(couponWalletService).addCouponWallet(email, couponId);

        ResultActions result = mockMvc.perform(post("/couponwallet/issuance/{couponId}", couponId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("쿠폰이 발급되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 ID번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 코드 발행 테스트")
    void testCouponWalletCodeAdd() throws Exception {
        //given
        Long couponQuantity = 20L;
        LocalDateTime startAt = LocalDateTime.of(2024, 9, 14, 00, 00, 00);
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 30, 23, 59, 59);
        LocalTime startTime = LocalTime.of(00, 00);
        LocalTime endTime = LocalTime.of(23, 59, 59);

        CouponSaveRequest request = CouponSaveRequest.builder()
                .issuerCode(1L)
                .name("보물찾기 이벤트 코드 쿠폰")
                .amountOrRate(3000)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .startTime(startTime)
                .endTime(endTime)
                .minOrderPrice(13000L)
                .discountType(DiscountType.FIXED_DISCOUNT)
                .issuerType(IssuerType.ALL)
                .maxCnt(1000)
                .maxCntPerCus(1)
                .build();


        //when & then
        Mockito.doNothing().when(couponWalletService).addCouponCode(request, couponQuantity);

        ResultActions result = mockMvc.perform(post("/couponwallet/couponcode/create?couponQuantity={couponQuantity}", couponQuantity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("쿠폰코드가 발행되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 코드 업데이트(고객 입력) 테스트")
    void testCouponWalletCodeModify() throws Exception {
        //given
        String couponCode = UUID.randomUUID().toString();

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();


        //when & then
        Mockito.doNothing().when(couponWalletService).modifyCouponCode(email, couponCode);

        ResultActions result = mockMvc.perform(patch("/couponwallet/couponcode/registration?couponCode={couponCode}", couponCode)
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("쿠폰이 등록되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                                parameterWithName("couponCode").description("쿠폰 코드")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 리스트 조회 테스트")
    void testCouponWalletList() throws Exception {
        //given
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 30, 23, 59, 59);

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        List<CouponWalletFindAllResponse> response = List.of(
                new CouponWalletFindAllResponse(3000, "첫 주문 쿠폰", 13000L, 15L, expiredAt),
                new CouponWalletFindAllResponse(3500, "브랜드 치킨 할인쿠폰 BBQ", 20000L, 15L, expiredAt),
                new CouponWalletFindAllResponse(4000, "브랜드 치킨 할인쿠폰 BHC", 23000L, 15L, expiredAt)
        );


        Mockito.when(couponWalletService.findCouponWallet(email)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/couponwallet/list")
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].amount").value(3000))
                .andExpect(jsonPath("$.data[0].name").value("첫 주문 쿠폰"))
                .andExpect(jsonPath("$.data[0].minOrderPrice").value(13000))
                .andExpect(jsonPath("$.data[0].leftDays").value(15))
                .andExpect(jsonPath("$.data[0].expiredAt").value("2024-09-30T23:59:59"))
                .andExpect(jsonPath("$.data[1].amount").value(3500))
                .andExpect(jsonPath("$.data[1].name").value("브랜드 치킨 할인쿠폰 BBQ"))
                .andExpect(jsonPath("$.data[1].minOrderPrice").value(20000))
                .andExpect(jsonPath("$.data[1].leftDays").value(15))
                .andExpect(jsonPath("$.data[1].expiredAt").value("2024-09-30T23:59:59"))
                .andExpect(jsonPath("$.data[2].amount").value(4000))
                .andExpect(jsonPath("$.data[2].name").value("브랜드 치킨 할인쿠폰 BHC"))
                .andExpect(jsonPath("$.data[2].minOrderPrice").value(23000))
                .andExpect(jsonPath("$.data[2].leftDays").value(15))
                .andExpect(jsonPath("$.data[2].expiredAt").value("2024-09-30T23:59:59"))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data[].amount").type(JsonFieldType.NUMBER).description("할인 금액"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("쿠폰 이름"),
                                fieldWithPath("data[].minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                fieldWithPath("data[].leftDays").type(JsonFieldType.NUMBER).description("남은 유효기간 일수"),
                                fieldWithPath("data[].expiredAt").type(JsonFieldType.STRING).description("만료 일자"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }
}