package clone.coding.coupon.controller;

import clone.coding.coupon.dto.CustomUserDetails;
import clone.coding.coupon.dto.customer.CustomerPwUpdateRequest;
import clone.coding.coupon.dto.customer.CustomerSaveRequest;
import clone.coding.coupon.entity.customer.Customer;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.CustomerService;
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

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
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
    @DisplayName("회원가입 테스트")
    void testCustomerAdd() throws Exception {
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
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
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


    @Test
    @DisplayName("회원탈퇴 테스트")
    void testCustomerRemove() throws Exception {
        //when & then
        ResultActions result = mockMvc.perform(delete("/customer/withdraw")
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("회원탈퇴 되었습니다."))
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
    @DisplayName("비밀번호 변경 테스트")
    void testCustomerPwModify() throws Exception {
        //given
        CustomerPwUpdateRequest request = new CustomerPwUpdateRequest();
        request.setPassword("newPassword1!");

        //when & then
        ResultActions result = mockMvc.perform(patch("/customer/info/pw-change")
                .contentType(MediaType.APPLICATION_JSON)
                .header("access", "JWT-Token")
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("비밀번호가 변경되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
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
    void testCustomerAddressModify() throws Exception {
        //given
        String address = "인천";

        //when & then
        ResultActions result = mockMvc.perform(patch("/customer/info/address?address={address}", address)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("주소가 변경되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
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
    void testCustomerCheckEmailDuplication() throws Exception {
        //given
        String email = "test@email.com";

        //when & then
        ResultActions result = mockMvc.perform(get("/customer/sign-up-email/{email}", email)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("사용 가능한 이메일 입니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
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
    void testCustomerCheckNicknameDuplication() throws Exception {
        //given
        String nickname = "testuser";

        //when & then
        ResultActions result = mockMvc.perform(get("/customer/sign-up-nickname/{nickname}", nickname)
                .header("access", "JWT-Token"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("사용 가능한 닉네임 입니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
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