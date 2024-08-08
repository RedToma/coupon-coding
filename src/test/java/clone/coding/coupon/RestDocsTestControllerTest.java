package clone.coding.coupon;

import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestDocsTestController.class)
class RestDocsTestControllerTest extends AbstractRestDocsTests {

    @Test
    void RestDocsTest() throws Exception {
        mockMvc.perform(get("/restDocsTest")).andExpect(status().isOk());
    }

    @Test
    void paramTest() throws Exception {

        mockMvc.perform(get("/test/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-post",
                        pathParameters(parameterWithName("id").description("회원ID")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("age").type(JsonFieldType.STRING).description("나이"))));
    }
}