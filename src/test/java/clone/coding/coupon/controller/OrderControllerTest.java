package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.dto.menu.MenuFindAllResponse;
import clone.coding.coupon.dto.menu.MenuSaveAndUpdateRequest;
import clone.coding.coupon.dto.order.*;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.PaymentType;
import clone.coding.coupon.entity.customer.StatusType;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.OrderService;
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
import java.util.List;

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
class OrderControllerTest extends AbstractRestDocsTests {

    @MockBean
    private OrderService orderService;

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
    @DisplayName("주문 생성 테스트")
    void testOrderAdd() throws Exception {
        //given
        OrderSaveRequest request = new OrderSaveRequest(PaymentType.ONLINE, 1L, 20000);

        //when & then
        ResultActions result = mockMvc.perform(post("/order/new")
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token")
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주문이 생성되었습니다."))
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
    @DisplayName("주문생성 전 테스트")
    void testOrdering() throws Exception {
        //given
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 29, 23, 59, 59);
        List<OrderFindByOrderMenuResponse> orderMenuResponses = List.of(
                new OrderFindByOrderMenuResponse("후라이드 치킨", 1, 23000)
        );

        List<OrderFindByCouponResponse> couponResponses = List.of(
                new OrderFindByCouponResponse(1L, 1000, "첫 주문 쿠폰", localDateTime, 10000L),
                new OrderFindByCouponResponse(2L, 2000, "BBQ 브랜드 할인쿠폰", localDateTime, 11000L)
        );

        OrderMenuAndCouponFindAllResponse response = new OrderMenuAndCouponFindAllResponse();
        response.setOrderMenu(orderMenuResponses);
        response.setCoupon(couponResponses);
        response.setTotalAmount(23000);


        Mockito.when(orderService.listPurchaseOrder(email)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/order/ordering")
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderMenu[0].menuName").value("후라이드 치킨"))
                .andExpect(jsonPath("$.data.orderMenu[0].menuCnt").value(1))
                .andExpect(jsonPath("$.data.orderMenu[0].menuPrice").value(23000))
                .andExpect(jsonPath("$.data.coupon[0].couponWalletId").value(1))
                .andExpect(jsonPath("$.data.coupon[0].amount").value(1000))
                .andExpect(jsonPath("$.data.coupon[0].couponName").value("첫 주문 쿠폰"))
                .andExpect(jsonPath("$.data.coupon[0].expireAt").value("2024-09-29T23:59:59"))
                .andExpect(jsonPath("$.data.coupon[0].minOrderPrice").value(10000))
                .andExpect(jsonPath("$.data.coupon[1].couponWalletId").value(2))
                .andExpect(jsonPath("$.data.coupon[1].amount").value(2000))
                .andExpect(jsonPath("$.data.coupon[1].couponName").value("BBQ 브랜드 할인쿠폰"))
                .andExpect(jsonPath("$.data.coupon[1].expireAt").value("2024-09-29T23:59:59"))
                .andExpect(jsonPath("$.data.coupon[1].minOrderPrice").value(11000))
                .andExpect(jsonPath("$.data.totalAmount").value(23000))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data.orderMenu[].menuName").type(JsonFieldType.STRING).description("주문한 메뉴 이름"),
                                fieldWithPath("data.orderMenu[].menuCnt").type(JsonFieldType.NUMBER).description("주문한 메뉴 수량"),
                                fieldWithPath("data.orderMenu[].menuPrice").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("data.coupon[].couponWalletId").type(JsonFieldType.NUMBER).description("쿠폰 ID번호"),
                                fieldWithPath("data.coupon[].amount").type(JsonFieldType.NUMBER).description("쿠폰 할인 금액"),
                                fieldWithPath("data.coupon[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
                                fieldWithPath("data.coupon[].expireAt").type(JsonFieldType.STRING).description("쿠폰 만료 시간"),
                                fieldWithPath("data.coupon[].minOrderPrice").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
                                fieldWithPath("data.totalAmount").type(JsonFieldType.NUMBER).description("총 주문 금액"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void testOrderList() throws Exception {
        //given
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        List<OrderFindByMenuNameResponse> menuNameResponses = List.of(
                new OrderFindByMenuNameResponse("후라이드 치킨"),
                new OrderFindByMenuNameResponse("맛초킹")
        );

        List<OrderListFindAllResponse> response = List.of(
                new OrderListFindAllResponse(47000, StatusType.DELIVERED, "2024-09-14 00:19", "BHC 송도1동", menuNameResponses)
        );


        Mockito.when(orderService.listOrder(email)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/order/list")
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].totalAmount").value(47000))
                .andExpect(jsonPath("$.data[0].statusType").value("DELIVERED"))
                .andExpect(jsonPath("$.data[0].orderTime").value("2024-09-14 00:19"))
                .andExpect(jsonPath("$.data[0].storeName").value("BHC 송도1동"))
                .andExpect(jsonPath("$.data[0].menuList[0].menuName").value("후라이드 치킨"))
                .andExpect(jsonPath("$.data[0].menuList[1].menuName").value("맛초킹"))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data[].totalAmount").type(JsonFieldType.NUMBER).description("총 주문 금액"),
                                fieldWithPath("data[].statusType").type(JsonFieldType.STRING).description("주문 상태"),
                                fieldWithPath("data[].orderTime").type(JsonFieldType.STRING).description("주문 시간"),
                                fieldWithPath("data[].storeName").type(JsonFieldType.STRING).description("지점 이름"),
                                fieldWithPath("data[].menuList[].menuName").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("주문 목록 조회(사장) 테스트")
    void testOrderListStore() throws Exception {
        //given
        Long storeId = 1L;
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 9, 14, 00, 26, 31);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 9, 14, 00, 19, 9);

        List<OrderFindAllStoreOrderMenuResponse> orderMenuResponses1 = List.of(
                new OrderFindAllStoreOrderMenuResponse("후라이드 치킨", 1),
                new OrderFindAllStoreOrderMenuResponse("황금올리브 치킨", 1)
        );

        List<OrderFindAllStoreOrderMenuResponse> orderMenuResponses2 = List.of(
                new OrderFindAllStoreOrderMenuResponse("블랙페퍼 치킨", 1)
        );

        List<OrderFindAllByStoreResponse> response = List.of(
                new OrderFindAllByStoreResponse(PaymentType.ONLINE, orderMenuResponses1, localDateTime1, StatusType.COOKING, 48000, 0),
                new OrderFindAllByStoreResponse(PaymentType.OFFLINE, orderMenuResponses2, localDateTime2, StatusType.DELIVERING, 20000, 3000)
        );

        Mockito.when(orderService.listStoreOrder(storeId)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/order/store/list/{storeId}", storeId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].paymentType").value("ONLINE"))
                .andExpect(jsonPath("$.data[0].orderMenuList[0].menuName").value("후라이드 치킨"))
                .andExpect(jsonPath("$.data[0].orderMenuList[0].menuCnt").value(1))
                .andExpect(jsonPath("$.data[0].orderMenuList[1].menuName").value("황금올리브 치킨"))
                .andExpect(jsonPath("$.data[0].orderMenuList[1].menuCnt").value(1))
                .andExpect(jsonPath("$.data[0].orderTime").value("2024-09-14T00:26:31"))
                .andExpect(jsonPath("$.data[0].statusType").value("COOKING"))
                .andExpect(jsonPath("$.data[0].totalAmount").value(48000))
                .andExpect(jsonPath("$.data[0].discount").value(0))
                .andExpect(jsonPath("$.data[1].paymentType").value("OFFLINE"))
                .andExpect(jsonPath("$.data[1].orderMenuList[0].menuName").value("블랙페퍼 치킨"))
                .andExpect(jsonPath("$.data[1].orderMenuList[0].menuCnt").value(1))
                .andExpect(jsonPath("$.data[1].orderTime").value("2024-09-14T00:19:09"))
                .andExpect(jsonPath("$.data[1].statusType").value("DELIVERING"))
                .andExpect(jsonPath("$.data[1].totalAmount").value(20000))
                .andExpect(jsonPath("$.data[1].discount").value(3000))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("지점 ID번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data[].paymentType").type(JsonFieldType.STRING).description("결제 유형"),
                                fieldWithPath("data[].orderMenuList[].menuName").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("data[].orderMenuList[].menuCnt").type(JsonFieldType.NUMBER).description("메뉴 수량"),
                                fieldWithPath("data[].orderTime").type(JsonFieldType.STRING).description("주문 시간"),
                                fieldWithPath("data[].statusType").type(JsonFieldType.STRING).description("주문 상태"),
                                fieldWithPath("data[].totalAmount").type(JsonFieldType.NUMBER).description("총 주문 금액"),
                                fieldWithPath("data[].discount").type(JsonFieldType.NUMBER).description("할인 금액"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("주문 상태 변경(조리 중) 테스트")
    void testOrderStatusToCookingModify() throws Exception {
        //given
        Long orderId = 1L;
        Long arrivalExpectTime = 60L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/order/cooking-update/{orderId}?arrivalExpectTime={arrivalExpectTime}", orderId, arrivalExpectTime)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주문 상태가 조리 중으로 변경 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID번호")
                        ),
                        queryParameters(
                                parameterWithName("arrivalExpectTime").description("예상 도착시간(분)")
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
    @DisplayName("주문 상태 변경(배달 중) 테스트")
    void testOrderStatusToDeliveringModify() throws Exception {
        //given
        Long orderId = 1L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/order/delivering-update/{orderId}", orderId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주문 상태가 배달 중으로 변경 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID번호")
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
    @DisplayName("주문 상태 변경(배달 완료) 테스트")
    void testOrderStatusToDeliveredModify() throws Exception {
        //given
        Long orderId = 1L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/order/delivered-update/{orderId}", orderId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("배달이 완료 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID번호")
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
    @DisplayName("주문 취소(가게) 테스트")
    void testOrderStatusToCancelModify() throws Exception {
        //given
        Long orderId = 1L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/order/cancel-update/{orderId}", orderId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("가게 사정으로 인해 주문이 취소 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID번호")
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
    @DisplayName("주문 취소(고객) 테스트")
    void testOrderStatusToCustomerCancelModify() throws Exception {
        //given
        Long orderId = 1L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/order/customer-cancel-update/{orderId}", orderId)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주문이 취소 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }
}