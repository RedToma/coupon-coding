package clone.coding.coupon.controller;

import clone.coding.coupon.dto.brand.BrandFindByNameResponse;
import clone.coding.coupon.dto.store.StoreFindByNameResponse;
import clone.coding.coupon.dto.store.StoreSaveAndUpdateRequest;
import clone.coding.coupon.restdocs.AbstractRestDocsTests;
import clone.coding.coupon.service.StoreService;
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
class StoreControllerTest extends AbstractRestDocsTests {

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("지점 추가 테스트")
    void testStoreAdd() throws Exception {
        //given
        Long brandId = 1L;

        StoreSaveAndUpdateRequest request = new StoreSaveAndUpdateRequest();
        request.setStoreName("BHC 송도1동");
        request.setStoreNum("070-1232-4567");
        request.setAddress("송도");

        //when & then
        ResultActions result = mockMvc.perform(post("/store/new/{brandId}", brandId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("지점이 추가되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("brandId").description("브랜드 ID번호")
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
    @DisplayName("지점 조회 테스트")
    void testStoreDetails() throws Exception {
        //given
        String storeName = "송도";

        List<StoreFindByNameResponse> response = List.of(
                new StoreFindByNameResponse(1L, "BBQ 송도1동", "송도"),
                new StoreFindByNameResponse(2L, "BHC 송도1동", "송도")
        );

        Mockito.when(storeService.findStore(storeName)).thenReturn(response);

        //when & then
        ResultActions result = mockMvc.perform(get("/store/search?storeName={storeName}", storeName)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].storeName").value("BBQ 송도1동"))
                .andExpect(jsonPath("$.data[0].address").value("송도"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].storeName").value("BHC 송도1동"))
                .andExpect(jsonPath("$.data[1].address").value("송도"))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        queryParameters(
                                parameterWithName("storeName").description("검색할 지점 이름")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("지점 ID"),
                                fieldWithPath("data[].storeName").type(JsonFieldType.STRING).description("지점 이름"),
                                fieldWithPath("data[].address").type(JsonFieldType.STRING).description("지점 주소"),
                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 처리 시간")
                        )
                ));
    }

    @Test
    @DisplayName("지점 정보 변경 테스트")
    void testStoreModify() throws Exception {
        //given
        Long storeId = 1L;

        StoreSaveAndUpdateRequest request = new StoreSaveAndUpdateRequest();
        request.setStoreName("BHC 송도1동");
        request.setStoreNum("070-1234-5678");
        request.setAddress("서울");

        //when & then
        ResultActions result = mockMvc.perform(patch("/store/info/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("지점 정보가 변경되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("지점 ID번호")
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
    @DisplayName("지점 폐업 테스트")
    void testStoreClose() throws Exception {
        //given
        Long storeId = 1L;

        //when & then
        ResultActions result = mockMvc.perform(patch("/store/close-store/{storeId}", storeId));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("지점이 폐업되었습니다."))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(Preprocessors.prettyPrint()),
                        preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("지점 ID번호")
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