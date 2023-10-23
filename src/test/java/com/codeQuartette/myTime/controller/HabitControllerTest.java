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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(HabitController.class)
class HabitControllerTest extends AbstractRestDocsTests {

    @MockBean
    HabitServiceImpl habitService;
    @MockBean
    HabitHasMyDateServiceImpl habitHasMyMyDateService;

    @Test
    void createHabit() throws Exception {

        HabitDTO.Request request = HabitDTO.Request.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(2023, 12, 31))
                .repeatDay(new String[]{"MON", "WED"})
                .category(Category.EXERCISE)
                .categoryContent("자전거 타기")
                .isBlind(true)
                .build();

        mockMvc.perform(post("/habit?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("create-habit",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
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
    void update() throws Exception {
        HabitDTO.Request request = HabitDTO.Request.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(2023, 12, 31))
                .repeatDay(new String[]{"MON", "WED"})
                .category(Category.EXERCISE)
                .categoryContent("자전거 타기 수정")
                .isBlind(true)
                .build();

        mockMvc.perform(patch("/habit?userId=1&id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("update-habit",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
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
    void deleteHabit() throws Exception {
        mockMvc.perform(delete("/habit?id=1"))
                .andExpect(status().isOk())
                .andDo(document("delete-habit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @DisplayName("습관 조회하기")
    @Test
    void getHabitById() throws Exception {
        Habit habit = Habit.builder().id(1L)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 31))
                .repeatDay("[\"MON\", \"FRI\"]")
                .category(Category.EXERCISE)
                .categoryContent("운동")
                .isBlind(false)
                .build();

        when(habitService.findHabit(any())).thenReturn(habit);

        mockMvc.perform(get("/habit?id=1"))
                .andExpect(status().isOk())
                .andDo(document("find-habitId-habit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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

        mockMvc.perform(get("/habit?userId=1&date=2023-01-27"))
                .andExpect(status().isOk())
                .andDo(document("find-date-habit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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

        mockMvc.perform(get("/habit?userId=1&yearMonth=2023-10"))
                .andExpect(status().isOk())
                .andDo(document("find-yearMonth-habit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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