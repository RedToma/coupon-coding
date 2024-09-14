package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.dto.menu.MenuSaveAndUpdateRequest;
import clone.coding.coupon.dto.order.OrderFindByMenuNameResponse;
import clone.coding.coupon.dto.order.OrderListFindAllResponse;
import clone.coding.coupon.dto.order.OrderSaveRequest;
import clone.coding.coupon.dto.orderMenu.OrderMenuFindAllResponse;
import clone.coding.coupon.dto.orderMenu.OrderMenuSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.entity.customer.PaymentType;
import clone.coding.coupon.entity.customer.StatusType;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.OrderMenuService;
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
class OrderMenuControllerTest extends AbstractRestDocsTests {

    @MockBean
    private OrderMenuService orderMenuService;

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
    @DisplayName("장바구니 추가 테스트")
    void testOrderMenuAdd() throws Exception {
        //given
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        OrderMenuSaveRequest request = new OrderMenuSaveRequest(1L, 2);


        //when & then
        Mockito.doNothing().when(orderMenuService).addOrderMenu(request, email);

        ResultActions result = mockMvc.perform(post("/order-menu/new")
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token")
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("장바구니에 추가 되었습니다."))
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
    @DisplayName("장바구니 조회 테스트")
    void testOrderMenuList() throws Exception {
        //given
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        List<OrderMenuFindAllResponse> response = List.of(
                new OrderMenuFindAllResponse(1L, 10L, "후라이드 치킨", 1, 23000),
                new OrderMenuFindAllResponse(2L, 9L, "양념 치킨", 2, 48000)
        );


        Mockito.when(orderMenuService.findOrderMenu(email)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/order-menu/list")
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].orderMenuId").value(1))
                .andExpect(jsonPath("$.data[0].menuId").value(10))
                .andExpect(jsonPath("$.data[0].menuName").value("후라이드 치킨"))
                .andExpect(jsonPath("$.data[0].menuCnt").value(1))
                .andExpect(jsonPath("$.data[0].menuPrice").value(23000))
                .andExpect(jsonPath("$.data[1].orderMenuId").value(2))
                .andExpect(jsonPath("$.data[1].menuId").value(9))
                .andExpect(jsonPath("$.data[1].menuName").value("양념 치킨"))
                .andExpect(jsonPath("$.data[1].menuCnt").value(2))
                .andExpect(jsonPath("$.data[1].menuPrice").value(48000))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data[].orderMenuId").type(JsonFieldType.NUMBER).description("장바구니 ID번호"),
                                fieldWithPath("data[].menuId").type(JsonFieldType.NUMBER).description("메뉴 ID번호"),
                                fieldWithPath("data[].menuName").type(JsonFieldType.STRING).description("메뉴 이름"),
                                fieldWithPath("data[].menuCnt").type(JsonFieldType.NUMBER).description("메뉴 수량"),
                                fieldWithPath("data[].menuPrice").type(JsonFieldType.NUMBER).description("메뉴 가격"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("장바구니 수량 변경 테스트")
    void testOrderMenuModify() throws Exception {
        //given
        Long orderMenuId = 1L;
        int menuCnt = 3;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        //when & then
        Mockito.doNothing().when(orderMenuService).modifyOrderMenu(email, menuCnt, orderMenuId);

        ResultActions result = mockMvc.perform(patch("/order-menu/update/{orderMenuId}?menuCnt={menuCnt}", orderMenuId, menuCnt)
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("수량이 변경 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                                parameterWithName("menuCnt").description("변경할 메뉴 수량")
                        ),
                        pathParameters(
                                parameterWithName("orderMenuId").description("장바구니 ID번호")
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
    @DisplayName("장바구니 선택 메뉴 삭제 테스트")
    void testOrderMenuRemove() throws Exception {
        //given
        Long orderMenuId = 1L;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        //when & then
        Mockito.doNothing().when(orderMenuService).removeOrderMenu(email, orderMenuId);
        ResultActions result = mockMvc.perform(delete("/order-menu/remove/{orderMenuId}", orderMenuId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("선택한 메뉴가 삭제 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("orderMenuId").description("장바구니 ID번호")
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