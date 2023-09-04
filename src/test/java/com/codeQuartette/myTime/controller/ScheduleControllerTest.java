package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.ScheduleDTO;
import com.codeQuartette.myTime.domain.Schedule;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(ScheduleController.class)
@ExtendWith(RestDocumentationExtension.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("스케줄 단건 조회")
    void find_scheduleId() throws Exception {
        List<Schedule> schedules = Arrays.asList(Schedule.builder()
                .id(1L)
                .title("아침밥 먹기")
                .color(Color.ADC4FF)
                .startDateTime(LocalDateTime.of(2023, 8, 30, 9, 0, 0))
                .endDateTime(LocalDateTime.of(2023, 8, 30, 10, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build());


        given(scheduleService.find(any())).willReturn(schedules);

        mvc.perform(RestDocumentationRequestBuilders.get("/schedule")
                .param("scheduleId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("schedules").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.title == '%s')]", "아침밥 먹기").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.color == '%s')]", "ADC4FF").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.startDate == '%s')]", "2023-08-30T09:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.endDate == '%s')]", "2023-08-30T10:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.isSpecificTime == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$..schedules[0][?(@.alert == %b)]", Boolean.TRUE).exists())
                .andDo(print())
                .andDo(document("find-scheduleId-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        queryParameters(
                                parameterWithName("scheduleId").description("스케줄 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields()
                                .andWithPrefix("schedules.[0]", fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부"))
                ));
    }

    @Test
    @DisplayName("스케줄 날짜별 조회")
    void find_date() throws Exception {
        List<Schedule> schedules = Arrays.asList(Schedule.builder()
                .id(1L)
                .title("아침밥 먹기")
                .color(Color.ADC4FF)
                .startDateTime(LocalDateTime.of(2023, 8, 30, 9, 0, 0))
                .endDateTime(LocalDateTime.of(2023, 8, 30, 10, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build());

        given(scheduleService.find(any(Long.class), any(LocalDate.class))).willReturn(schedules);

        mvc.perform(RestDocumentationRequestBuilders.get("/schedule")
                .param("userId", "1")
                .param("date", "2023-08-30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("schedules").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.title == '%s')]", "아침밥 먹기").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.color == '%s')]", "ADC4FF").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.startDate == '%s')]", "2023-08-30T09:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.endDate == '%s')]", "2023-08-30T10:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.isSpecificTime == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$..schedules[0][?(@.alert == %b)]", Boolean.TRUE).exists())
                .andDo(print())
                .andDo(document("find-date-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        queryParameters(
                                parameterWithName("date").description("날짜"),
                                parameterWithName("userId").description("유저 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields()
                                .andWithPrefix("schedules.[0]", fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부"))
                ));
    }

    @Test
    @DisplayName("스케줄 연도별 조회")
    void find_yearMonth() throws Exception {
        List<Schedule> schedules = Arrays.asList(Schedule.builder()
                .id(1L)
                .title("아침밥 먹기")
                .color(Color.ADC4FF)
                .startDateTime(LocalDateTime.of(2023, 8, 30, 9, 0, 0))
                .endDateTime(LocalDateTime.of(2023, 8, 30, 10, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build());

        given(scheduleService.find(any(Long.class), any(YearMonth.class))).willReturn(schedules);

        mvc.perform(RestDocumentationRequestBuilders.get("/schedule")
                .param("userId", "1")
                .param("yearMonth", "2023-09")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("schedules").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.title == '%s')]", "아침밥 먹기").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.color == '%s')]", "ADC4FF").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.startDate == '%s')]", "2023-08-30T09:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.endDate == '%s')]", "2023-08-30T10:00:00").exists())
                .andExpect(jsonPath("$..schedules[0][?(@.isSpecificTime == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$..schedules[0][?(@.alert == %b)]", Boolean.TRUE).exists())
                .andDo(print())
                .andDo(document("find-yearMonth-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        queryParameters(
                                parameterWithName("yearMonth").description("년월"),
                                parameterWithName("userId").description("유저 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields()
                                .andWithPrefix("schedules.[0]", fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"))
                                .andWithPrefix("schedules.[0]", fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부"))
                ));
    }

    @Test
    @DisplayName("스케줄 등록")
    void create() throws Exception {
        ScheduleDTO.Request request = ScheduleDTO.Request.builder()
                .title("아침밥 먹기")
                .color(Color.ADC4FF)
                .startDate(LocalDateTime.of(2023, 8, 30, 9, 0, 0))
                .endDate(LocalDateTime.of(2023, 8, 30, 10, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build();

        mvc.perform(RestDocumentationRequestBuilders.post("/schedule")
                .param("userId", "1")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("create-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"),
                                fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"),
                                fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부")
                        )));
    }

    @Test
    @DisplayName("스케줄 수정")
    void update() throws Exception {
        ScheduleDTO.Request request = ScheduleDTO.Request.builder()
                .title("점심밥 먹기")
                .color(Color.ADC4FF)
                .startDate(LocalDateTime.of(2023, 8, 30, 12, 0, 0))
                .endDate(LocalDateTime.of(2023, 8, 30, 13, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build();

        Schedule schedule = Schedule.builder()
                .id(1L)
                .title("점심밥 먹기")
                .color(Color.ADC4FF)
                .startDateTime(LocalDateTime.of(2023, 8, 30, 12, 00, 00))
                .endDateTime(LocalDateTime.of(2023, 8, 30, 13, 00, 00))
                .isSpecificTime(Boolean.FALSE)
                .alert(Boolean.TRUE)
                .build();

        given(scheduleService.update(any(Long.class), any(Long.class), any())).willReturn(schedule);

        mvc.perform(RestDocumentationRequestBuilders.put("/schedule")
                .param("userId", "1")
                .param("id", "1")
                .with(csrf().asHeader())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[?(@.id == '%s')]", "1").exists())
                .andExpect(jsonPath("$.[?(@.title == '%s')]", "점심밥 먹기").exists())
                .andExpect(jsonPath("$.[?(@.color == '%s')]", "ADC4FF").exists())
                .andExpect(jsonPath("$.[?(@.startDate == '%s')]", "2023-08-30T12:00:00").exists())
                .andExpect(jsonPath("$.[?(@.endDate == '%s')]", "2023-08-30T13:00:00").exists())
                .andExpect(jsonPath("$.[?(@.isSpecificTime == %b)]", Boolean.FALSE).exists())
                .andExpect(jsonPath("$.[?(@.alert == %b)]", Boolean.TRUE).exists())
                .andDo(print())
                .andDo(document("update-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        queryParameters(
                                parameterWithName("userId").description("유저 아이디"),
                                parameterWithName("id").description("스케줄 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"),
                                fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"),
                                fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("스케줄 아이디"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("스케줄 제목"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("스케줄 색상"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("스케줄 시작 날짜 및 시간"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("스케줄 끝 날짜 및 시간"),
                                fieldWithPath("isSpecificTime").type(JsonFieldType.BOOLEAN).description("스케줄 시작 시간 여부"),
                                fieldWithPath("alert").type(JsonFieldType.BOOLEAN).description("유저 알림 여부"))));

    }

    @Test
    @DisplayName("스케줄 삭제")
    void delete() throws Exception {

        mvc.perform(RestDocumentationRequestBuilders.delete("/schedule")
                .param("userId", "1")
                .param("id", "1")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-schedule",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        queryParameters(
                                parameterWithName("userId").description("유저 아이디").optional(),
                                parameterWithName("id").description("스케줄 아이디").optional()
                        )));
    }
}