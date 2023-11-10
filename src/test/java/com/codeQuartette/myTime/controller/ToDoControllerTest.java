package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.AbstractRestDocsTests;
import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(ToDoController.class)
class ToDoControllerTest extends AbstractRestDocsTests {

    @MockBean
    ToDoServiceImpl toDoService;

    @Test
    @DisplayName("할일 단건 조회")
    void find_ToDoId() throws Exception{
        List<ToDo> toDos = Collections.singletonList(ToDo.builder()
                .id(1L)
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build());

        given(toDoService.find(any(), (Long)any())).willReturn(toDos);

        mockMvc.perform(get("/todo")
                        .param("id", "1")
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("response.toDos").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.title == '%s')]", "방 만들기").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.color == '%s')]", "FFADAD").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.date == '%s')]", "2023-11-02").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.isDone == %b)]", Boolean.TRUE).exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.isBlind == %b)]", Boolean.FALSE).exists())
                .andDo(print())
                .andDo(document("find-todoId-todo",
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                                ),
                                queryParameters(
                                        parameterWithName("id").description("할 일 아이디")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"))
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("id").type(JsonFieldType.NUMBER).description("할 일 아이디").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("color").type(JsonFieldType.STRING).description("색깔").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("date").type(JsonFieldType.STRING).description("날짜").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("isDone").type(JsonFieldType.BOOLEAN).description("할 일 완료 여부").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("할 일 노출 여부").optional())
                        )
                );
    }

    @Test
    @DisplayName("할 일 날짜별 조회")
    void findToDoDate() throws Exception{
        List<ToDo> toDos = Collections.singletonList(ToDo.builder()
                .id(1L)
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build());

        given(toDoService.find(any(), (LocalDate) any())).willReturn(toDos);

        mockMvc.perform(get("/todo")
                        .param("date", "2023-11-02")
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("response.toDos").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.title == '%s')]", "방 만들기").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.color == '%s')]", "FFADAD").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.date == '%s')]", "2023-11-02").exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.isDone == %b)]", Boolean.TRUE).exists())
                .andExpect(jsonPath("$..response.toDos[0][?(@.isBlind == %b)]", Boolean.FALSE).exists())
                .andDo(print())
                .andDo(document("find-date-todo",
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                                ),
                                queryParameters(
                                        parameterWithName("date").description("할 일 날짜")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"))
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("id").type(JsonFieldType.NUMBER).description("할 일 아이디").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("color").type(JsonFieldType.STRING).description("색깔").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("date").type(JsonFieldType.STRING).description("날짜").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("isDone").type(JsonFieldType.BOOLEAN).description("할 일 완료 여부").optional())
                                        .andWithPrefix("response.toDos.[0]",fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("할 일 노출 여부").optional())
                        )
                );

    }

    @Test
    @DisplayName("할 일 생성")
    void createToDo() throws Exception {
        String accessToken = "Bearer Access Token";

        ToDoDTO.Request request = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.now())
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        mockMvc.perform(post("/todo")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("code").value(201))
                .andExpect(jsonPath("message").value("Created"))
                .andDo(print())
                .andDo(document("create-todo",
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("color").type(JsonFieldType.STRING).description("색깔"),
                                        fieldWithPath("date").type(JsonFieldType.STRING).description("날짜"),
                                        fieldWithPath("isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                        fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("할 일 노출 여부")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지")
                                )
                        )
                );
        }

    @Test
    @DisplayName("할 일 수정")
    void update() throws Exception {
        ToDoDTO.Request request = ToDoDTO.Request.builder()
                .title("방 수정하기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        ToDo toDo = ToDo.builder()
                .id(1L)
                .title("방 수정하기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        given(toDoService.update(any(), any(), any())).willReturn(toDo);

        mockMvc.perform(patch("/todo")
                        .param("id", "1")
                        .with(csrf().asHeader())
                        .header("Authorization", "Bearer accessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("response").exists())
                .andExpect(jsonPath("$.response.[?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$.response.[?(@.title == '%s')]", "방 수정하기").exists())
                .andExpect(jsonPath("$.response.[?(@.color == '%s')]", Color.FFADAD).exists())
                .andExpect(jsonPath("$.response.[?(@.date == '%s')]", "2023-11-02").exists())
                .andExpect(jsonPath("$.response.[?(@.isDone == %b)]", Boolean.TRUE).exists())
                .andExpect(jsonPath("$.response.[?(@.isBlind == %b)]", Boolean.FALSE).exists())
                .andDo(print())
                .andDo(document("update-todo",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("id").description("할 일 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("색깔"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("할 일 노출 여부")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
                                fieldWithPath("response.id").type(JsonFieldType.NUMBER).description("할 일 아이디"),
                                fieldWithPath("response.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("response.color").type(JsonFieldType.STRING).description("색깔"),
                                fieldWithPath("response.date").type(JsonFieldType.STRING).description("날짜"),
                                fieldWithPath("response.isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                fieldWithPath("response.isBlind").type(JsonFieldType.BOOLEAN).description("할 일 노출 여부")
                        )
                )
        );
    }

    @Test
    @DisplayName("할 일 삭제")
    void delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/todo")
                        .param("id", "1")
                        .with(csrf().asHeader())
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andDo(print())
                .andDo(document("delete-todo",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("id").description("할 일 아이디").optional()
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지")
                        )
                    )
                );
    }
}
