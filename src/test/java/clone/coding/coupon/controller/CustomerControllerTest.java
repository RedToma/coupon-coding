package clone.coding.coupon.controller;

import clone.coding.coupon.dto.customer.CustomerLoginRequest;
import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest extends AbstractRestDocsTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("회원가입 테스트")
    void customerAdd() throws Exception {
        //given
        CustomerSaveRequest request = new CustomerSaveRequest();
        request.setEmail("test@email.com");
        request.setPassword("Password1!");
        request.setNickname("testuser");
        request.setAddress("서울");
        request.setPhoneNum("010-1234-5678");

        //when & then
        ResultActions result = mockMvc.perform(post("/customer/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("회원가입이 완료되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-add",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("회원 주소"),
                                fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("회원 휴대폰 번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

//    @Test
//    @DisplayName("로그인 테스트")
//    void customerLoginDetails() throws Exception {
//        //given
//        CustomerLoginRequest request = new CustomerLoginRequest();
//        request.setEmail("test@email.com");
//        request.setPassword("Password1!");
//
//        //when & then
//        ResultActions result = mockMvc.perform(get("/customer/log-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").value("로그인 되었습니다."))
//                .andExpect(jsonPath("$.error", is(nullValue())))
//                .andDo(document("customer-login",
//                        requestFields(
//                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
//                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
//                                fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
//                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
//                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
//                        )
//                ));
//    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void customerRemove() throws Exception {
        //given
        Long id = 1L;

        //when & then
        ResultActions result = mockMvc.perform(delete("/customer/withdraw/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("회원탈퇴 되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-remove",
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
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
    @DisplayName("비밀번호 변경 테스트")
    void customerPwModify() throws Exception {
        //given
        Long id = 1L;
        CustomerPwUpdateRequest request = new CustomerPwUpdateRequest();
        request.setPassword("Password12!!");

        //when & then
        ResultActions result = mockMvc.perform(patch("/customer/info/pw-change/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("비밀번호가 변경되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-pw-change",
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("변경할 비밀번호")
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
    @DisplayName("주소 변경 테스트")
    void customerAddressModify() throws Exception {
        //given
        Long id = 1L;
        String address = "인천";

        //when & then
        ResultActions result = mockMvc.perform(patch("/customer/info/address/{id}?address={address}", id, address));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주소가 변경되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-address-update",
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        queryParameters(
                                parameterWithName("address").description("변경할 주소")
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
    @DisplayName("이메일 중복 테스트")
    void customerCheckEmailDuplication() throws Exception {
        //given
        String email = "test@email.com";
        Mockito.when(customerService.checkEmailDuplication(email)).thenReturn(false);

        //when & then
        ResultActions result = mockMvc.perform(get("/customer/sign-up-email/{email}", email));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("사용 가능한 이메일 입니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-check-email-duplication",
                        pathParameters(
                                parameterWithName("email").description("중복 확인할 이메일")
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
    @DisplayName("닉네임 중복 테스트")
    void customerCheckNicknameDuplication() throws Exception {
        //given
        String nickname = "testuser";
        Mockito.when(customerService.checkNicknameDuplication(nickname)).thenReturn(false);

        //when & then
        ResultActions result = mockMvc.perform(get("/customer/sign-up-nickname/{nickname}", nickname));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("사용 가능한 닉네임 입니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("customer-check-nickname-duplication",
                        pathParameters(
                                parameterWithName("nickname").description("중복 확인할 닉네임")
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