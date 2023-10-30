package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.AbstractRestDocsTests;
import com.codeQuartette.myTime.controller.dto.HabitDTO;
import com.codeQuartette.myTime.domain.Habit;
import com.codeQuartette.myTime.domain.HabitHasMyDate;
import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.domain.value.Category;
import com.codeQuartette.myTime.service.impl.HabitHasMyDateServiceImpl;
import com.codeQuartette.myTime.service.impl.HabitServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@WebMvcTest(HabitController.class)
class HabitControllerTest extends AbstractRestDocsTests {

    @MockBean
    HabitServiceImpl habitService;
    @MockBean
    HabitHasMyDateServiceImpl habitHasMyMyDateService;

    @Test
    @DisplayName("습관 생성하기")
    void createHabit() throws Exception {

        HabitDTO.Request request = HabitDTO.Request.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(2023, 12, 31))
                .repeatDay(new String[]{"MON", "WED"})
                .category(Category.EXERCISE)
                .categoryContent("자전거 타기")
                .isBlind(true)
                .build();

        mockMvc.perform(post("/habit")
                        .with(csrf())
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("code").value(201))
                .andExpect(jsonPath("message").value("Created"))
                .andDo(print())
                .andDo(document("create-habit",
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                                ),
                                requestFields(
                                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("끝 일자"),
                                        fieldWithPath("repeatDay").type(JsonFieldType.ARRAY).description("반복 요일"),
                                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                        fieldWithPath("categoryContent").type(JsonFieldType.STRING).description("카테고리 세부 정보"),
                                        fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("캘린더 노출 여부")

                                )

                        )
                );
    }

    @Test
    @DisplayName("습관 수정하기")
    void update() throws Exception {
        HabitDTO.Request request = HabitDTO.Request.builder()
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .repeatDay(new String[]{"MON", "WED"})
                .category(Category.EXERCISE)
                .categoryContent("자전거 타기 수정")
                .isBlind(true)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .repeatDay("[\"MON\", \"WED\"]")
                .category(request.getCategory())
                .categoryContent(request.getCategoryContent())
                .isBlind(request.getIsBlind())
                .build();

        when(habitService.update(any(), any(), any())).thenReturn(habit);

        mockMvc.perform(patch("/habit")
                        .param("id", "1")
                        .with(csrf().asHeader())
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$..response[?(@.startDate == '%s')]", "2023-10-01").exists())
                .andExpect(jsonPath("$..response[?(@.endDate == '%s')]", "2023-12-31").exists())
                .andExpect(jsonPath("$..response.repeatDay[0,1]", "[\"MON\",\"WED\"]").exists())
                .andExpect(jsonPath("$..response[?(@.category == '%s')]", "EXERCISE").exists())
                .andExpect(jsonPath("$..response[?(@.categoryContent == '%s')]", "자전거 타기 수정").exists())
                .andExpect(jsonPath("$..response[?(@.blind == %b)]", Boolean.TRUE).exists())
                .andDo(print())
                .andDo(document("update-habit",
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                                ),
                                queryParameters(
                                        parameterWithName("id").description("습관 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 일자"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("끝 일자"),
                                        fieldWithPath("repeatDay").type(JsonFieldType.ARRAY).description("반복 요일"),
                                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                        fieldWithPath("categoryContent").type(JsonFieldType.STRING).description("카테고리 세부 정보"),
                                        fieldWithPath("isBlind").type(JsonFieldType.BOOLEAN).description("캘린더 노출 여부")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시"),
                                        fieldWithPath("response.id").type(JsonFieldType.NUMBER).description("습관 ID"),
                                        fieldWithPath("response.startDate").type(JsonFieldType.STRING).description("습관 시작 날짜"),
                                        fieldWithPath("response.endDate").type(JsonFieldType.STRING).description("습관 종료 날짜"),
                                        fieldWithPath("response.repeatDay").type(JsonFieldType.ARRAY).description("습관 반복 요일"),
                                        fieldWithPath("response.category").type(JsonFieldType.STRING).description("카테고리"),
                                        fieldWithPath("response.categoryContent").type(JsonFieldType.STRING).description("카테고리 상세 내용"),
                                        fieldWithPath("response.blind").type(JsonFieldType.BOOLEAN).description("달력 노출 여부")
                                )
                        )
                );
    }

    @Test
    @DisplayName("습관 삭제하기")
    void deleteHabit() throws Exception {
        mockMvc.perform(delete("/habit")
                        .param("id", "1")
                        .with(csrf().asHeader())
                        .header("Authorization", "accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andDo(print())
                .andDo(document("delete-habit",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("id").description("습관 아이디").optional()
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"))));
    }

    @Test
    @DisplayName("습관 단건 조회하기")
    void getHabitById() throws Exception {
        Habit habit = Habit.builder().id(1L)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 31))
                .repeatDay("[\"MON\", \"FRI\"]")
                .category(Category.EXERCISE)
                .categoryContent("운동")
                .isBlind(false)
                .build();

        when(habitService.findHabit(any(), any())).thenReturn(habit);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/habit")
                        .param("id", "1")
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$..response[?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..response[?(@.startDate == '%s')]", "2023-01-01").exists())
                .andExpect(jsonPath("$..response[?(@.endDate == '%s')]", "2023-01-31").exists())
                .andExpect(jsonPath("$..response.repeatDay[0,1]", "[\"MON\",\"FRI\"]").exists())
                .andExpect(jsonPath("$..response[?(@.category == '%s')]", "EXERCISE").exists())
                .andExpect(jsonPath("$..response[?(@.categoryContent == '%s')]", "운동").exists())
                .andExpect(jsonPath("$..response[?(@.blind == %b)]", Boolean.FALSE).exists())
                .andDo(print())
                .andDo(document("find-habitId-habit",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("id").description("habit id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시"),
                                fieldWithPath("response.id").type(JsonFieldType.NUMBER).description("습관 ID"),
                                fieldWithPath("response.startDate").type(JsonFieldType.STRING).description("습관 시작 날짜"),
                                fieldWithPath("response.endDate").type(JsonFieldType.STRING).description("습관 종료 날짜"),
                                fieldWithPath("response.repeatDay").type(JsonFieldType.ARRAY).description("습관 반복 요일"),
                                fieldWithPath("response.category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("response.categoryContent").type(JsonFieldType.STRING).description("카테고리 상세 내용"),
                                fieldWithPath("response.blind").type(JsonFieldType.BOOLEAN).description("달력 노출 여부")
                        )
                ));
    }

    @Test
    @DisplayName("습관 날짜별 조회하기")
    void getHabitByDate() throws Exception {
        Habit habit = Habit.builder().id(1L)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 31))
                .repeatDay("[\"MON\", \"FRI\"]")
                .category(Category.EXERCISE)
                .categoryContent("운동")
                .isBlind(false)
                .build();

        User user = User.builder()
                .name("testName")
                .nickname("testNickname")
                .email("testEmail@gmail.com")
                .birthday(LocalDate.of(2000, 2, 22))
                .profileImage("http://ProfileImage.jpg")
                .gender(false)
                .build();

        MyDate mydate = MyDate.builder()
                .id(1L)
                .date(LocalDate.of(2023, 1, 27))
                .user(user)
                .build();

        HabitHasMyDate habitHasMyDate = HabitHasMyDate.builder()
                .habit(habit)
                .myDate(mydate)
                .isDone(true)
                .build();

        List<HabitHasMyDate> habitHasMyDateList = new ArrayList<>();
        habitHasMyDateList.add(habitHasMyDate);

        when(habitHasMyMyDateService.findAllHabitHasMyDate(any(), (LocalDate) any())).thenReturn(habitHasMyDateList);

        mockMvc.perform(get("/habit")
                        .param("date", "2023-01-27")
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$..response[?(@.habitId == '%s')]", "1").exists())
                .andExpect(jsonPath("$..response[?(@.startDate == '%s')]", "2023-01-01").exists())
                .andExpect(jsonPath("$..response[?(@.endDate == '%s')]", "2023-01-31").exists())
                .andExpect(jsonPath("$..response[?(@.isBlind == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$..response[?(@.category == '%s')]", "EXERCISE").exists())
                .andExpect(jsonPath("$..response[?(@.categoryContent == '%s')]", "운동").exists())
                .andExpect(jsonPath("$..response[?(@.date == '%s')]", "2023-01-27").exists())
                .andExpect(jsonPath("$..response[?(@.done == %b)]", Boolean.TRUE).exists())
                .andDo(document("find-date-habit",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("date").description("습관을 조회하려는 날짜")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("response.[].habitId").type(JsonFieldType.NUMBER).description("습관 ID"),
                                fieldWithPath("response.[].startDate").type(JsonFieldType.STRING).description("습관 시작 날짜"),
                                fieldWithPath("response.[].endDate").type(JsonFieldType.STRING).description("습관 종료 날짜"),
                                fieldWithPath("response.[].isBlind").type(JsonFieldType.BOOLEAN).description("습관 공개 여부"),
                                fieldWithPath("response.[].category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("response.[].categoryContent").type(JsonFieldType.STRING).description("카테고리 상세 내용"),
                                fieldWithPath("response.[].date").type(JsonFieldType.STRING).description("습관을 이행하는 날짜"),
                                fieldWithPath("response.[].done").type(JsonFieldType.BOOLEAN).description("완료 여부")
                        )
                ));
    }

    @Test
    @DisplayName("습관 월별 조회하기")
    void getHabitByMonth() throws Exception {
        Habit habit = Habit.builder().id(1L)
                .startDate(LocalDate.of(2023, 10, 13))
                .endDate(LocalDate.of(2025, 10, 13))
                .repeatDay("[\"MON\", \"FRI\"]")
                .category(Category.EXERCISE)
                .categoryContent("운동")
                .isBlind(false)
                .build();

        User user = User.builder()
                .name("testName")
                .nickname("testNickname")
                .email("testEmail@gmail.com")
                .birthday(LocalDate.of(2000, 2, 22))
                .profileImage("http://ProfileImage.jpg")
                .gender(false)
                .build();

        MyDate mydate1013 = MyDate.builder()
                .id(1L)
                .date(LocalDate.of(2023, 10, 13))
                .user(user)
                .build();

        MyDate mydate1016 = MyDate.builder()
                .id(1L)
                .date(LocalDate.of(2023, 10, 16))
                .user(user)
                .build();

        HabitHasMyDate habitHasMyDate1 = HabitHasMyDate.builder()
                .habit(habit)
                .myDate(mydate1013)
                .isDone(true)
                .build();

        HabitHasMyDate habitHasMyDate2 = HabitHasMyDate.builder()
                .habit(habit)
                .myDate(mydate1016)
                .isDone(false)
                .build();

        List<HabitHasMyDate> habitHasMyDateList = new ArrayList<>();
        habitHasMyDateList.add(habitHasMyDate1);
        habitHasMyDateList.add(habitHasMyDate2);

        when(habitHasMyMyDateService.findAllHabitHasMyDate(any(), (YearMonth) any())).thenReturn(habitHasMyDateList);

        mockMvc.perform(get("/habit")
                        .param("yearMonth", "2023-10")
                        .header("Authorization", "Bearer accessToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("$..response[?(@.habitId == '%s')]", "1").exists())
                .andExpect(jsonPath("$..response[?(@.startDate == '%s')]", "2023-10-13").exists())
                .andExpect(jsonPath("$..response[?(@.endDate == '%s')]", "2025-10-13").exists())
                .andExpect(jsonPath("$..response[?(@.isBlind == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$..response[?(@.category == '%s')]", "EXERCISE").exists())
                .andExpect(jsonPath("$..response[?(@.categoryContent == '%s')]", "운동").exists())
                .andExpect(jsonPath("$..response[?(@.date == '%s')]", "2023-10-13").exists())
                .andExpect(jsonPath("$..response[?(@.done == %b)]", Boolean.TRUE).exists())
                .andDo(document("find-yearMonth-habit",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("USER TOKEN")
                        ),
                        queryParameters(
                                parameterWithName("yearMonth").description("습관을 조회하려는 년월")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("response.[].habitId").type(JsonFieldType.NUMBER).description("습관 ID"),
                                fieldWithPath("response.[].startDate").type(JsonFieldType.STRING).description("습관 시작 날짜"),
                                fieldWithPath("response.[].endDate").type(JsonFieldType.STRING).description("습관 종료 날짜"),
                                fieldWithPath("response.[].isBlind").type(JsonFieldType.BOOLEAN).description("습관 공개 여부"),
                                fieldWithPath("response.[].category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("response.[].categoryContent").type(JsonFieldType.STRING).description("카테고리 상세 내용"),
                                fieldWithPath("response.[].date").type(JsonFieldType.STRING).description("습관을 이행하는 날짜"),
                                fieldWithPath("response.[].done").type(JsonFieldType.BOOLEAN).description("완료 여부")
                        )
                ));
    }

    @Test
    void getCategory() throws Exception {
        List<String> categoryList = Arrays.stream(Category.values()).map(category -> category.toString()).toList();

        when(habitService.getCategory()).thenReturn(categoryList);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/category"))
                .andExpect(status().isOk())
                .andDo(document("getCategory",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("response.[]").type(JsonFieldType.ARRAY).description("습관 카테고리 종류")
                        )));
    }
}
