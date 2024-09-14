package clone.coding.coupon.controller;

import clone.coding.coupon.dto.coupon.CouponSaveRequest;
import clone.coding.coupon.entity.coupon.DiscountType;
import clone.coding.coupon.entity.coupon.IssuerType;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest extends AbstractRestDocsTests {

    @MockBean
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("어드민 쿠폰 발행 테스트")
    void couponAdminAdd() throws Exception {
        //given
        LocalDateTime startAt = LocalDateTime.of(2024, 9, 14, 00, 00, 00);
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 30, 23, 59, 59);
        LocalTime startTime = LocalTime.of(00, 00);
        LocalTime endTime = LocalTime.of(23, 59, 59);

        CouponSaveRequest request = CouponSaveRequest.builder()
                .issuerCode(1L)
                .name("첫 구매 이벤트 쿠폰")
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
        Mockito.doNothing().when(couponService).addAdminCoupon(request);

        ResultActions result = mockMvc.perform(post("/coupon/admin/issuer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("어드민 쿠폰이 정상적으로 발급되었습니다."))
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
    @DisplayName("브랜드 쿠폰 발행 테스트")
    void couponBrandAdd() throws Exception {
        //given
        LocalDateTime startAt = LocalDateTime.of(2024, 9, 14, 00, 00, 00);
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 30, 23, 59, 59);
        LocalTime startTime = LocalTime.of(00, 00);
        LocalTime endTime = LocalTime.of(23, 59, 59);

        CouponSaveRequest request = CouponSaveRequest.builder()
                .issuerCode(2L)
                .name("브랜드 치킨 할인쿠폰 BHC")
                .amountOrRate(2500)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .startTime(startTime)
                .endTime(endTime)
                .minOrderPrice(13000L)
                .discountType(DiscountType.FIXED_DISCOUNT)
                .issuerType(IssuerType.BRAND)
                .maxCnt(1000)
                .maxCntPerCus(2)
                .build();


        //when & then
        Mockito.doNothing().when(couponService).addBrandCoupon(request);

        ResultActions result = mockMvc.perform(post("/coupon/brand/issuer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("브랜드 쿠폰이 정상적으로 발급되었습니다."))
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
    @DisplayName("스토어 쿠폰 발행 테스트")
    void couponStoreAdd() throws Exception {
        //given
        LocalDateTime startAt = LocalDateTime.of(2024, 9, 14, 00, 00, 00);
        LocalDateTime expiredAt = LocalDateTime.of(2024, 9, 30, 23, 59, 59);
        LocalTime startTime = LocalTime.of(00, 00);
        LocalTime endTime = LocalTime.of(23, 59, 59);

        CouponSaveRequest request = CouponSaveRequest.builder()
                .issuerCode(3L)
                .storeName("BBQ 송도1동")
                .name("BBQ 송도1동 첫 주문쿠폰")
                .amountOrRate(2000)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .startTime(startTime)
                .endTime(endTime)
                .minOrderPrice(13000L)
                .discountType(DiscountType.FIXED_DISCOUNT)
                .issuerType(IssuerType.STORE)
                .maxCnt(1000)
                .maxCntPerCus(1)
                .build();


        //when & then
        Mockito.doNothing().when(couponService).addStoreCoupon(request);

        ResultActions result = mockMvc.perform(post("/coupon/store/issuer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("스토어 쿠폰이 정상적으로 발급되었습니다."))
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
}