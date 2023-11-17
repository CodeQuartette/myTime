package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.repository.ToDoRepository;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest
class ToDoServiceImplTest {

    @Autowired
    private ToDoServiceImpl toDoService;

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    @DisplayName("할 일 등록")
    void create() {
        Long userId = 1L;

        ToDoDTO.Request request = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        toDoService.create(userId, request);

        SoftAssertions softAssertions = new SoftAssertions();

        ToDo toDo = toDoRepository.findById(userId).get();

        softAssertions.assertThat(toDo.getTitle()).isEqualTo(request.getTitle());
        softAssertions.assertThat(toDo.getColor()).isEqualTo(request.getColor());
        softAssertions.assertThat(toDo.getDate()).isEqualTo(request.getDate());
        softAssertions.assertThat(toDo.getIsDone()).isEqualTo(request.getIsDone());
        softAssertions.assertThat(toDo.getIsBlind()).isEqualTo(request.getIsBlind());
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("할 일 수정")
    void update() {
        Long userId = 1L;

        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        ToDoDTO.Request update = ToDoDTO.Request.builder()
                .title("방 없애기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 11))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDoUpdate = toDoService.update(userId, toDo.getId(), update);
        toDo = toDoRepository.findById(toDo.getId()).get();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(toDo.getId()).isEqualTo(toDoUpdate.getId());
        softAssertions.assertThat(toDo.getTitle()).isEqualTo(toDoUpdate.getTitle());
        softAssertions.assertThat(toDo.getColor()).isEqualTo(toDoUpdate.getColor());
        softAssertions.assertThat(toDo.getDate()).isEqualTo(toDoUpdate.getDate());
        softAssertions.assertThat(toDo.getIsDone()).isEqualTo(toDoUpdate.getIsDone());
        softAssertions.assertThat(toDo.getIsBlind()).isEqualTo(toDoUpdate.getIsBlind());
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("할 일 삭제")
    void delete() {
        Long userId = 1L;
        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        SoftAssertions softAssertions = new SoftAssertions();

        toDoService.delete(userId, toDo.getId());
        toDo = toDoRepository.findById(toDo.getId()).orElse(null);
        softAssertions.assertThat(toDo).isNull();
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("할 일 조회")
    void find() {
        Long userId = 1L;
        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        toDoService.create(userId, create);
        ToDo findToDo = toDoRepository.findById(userId).get();
        List<ToDo> toDos = toDoService.find(userId, findToDo.getId());

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(toDos.get(0).getTitle()).isEqualTo(findToDo.getTitle());
        softAssertions.assertThat(toDos.get(0).getColor()).isEqualTo(findToDo.getColor());
        softAssertions.assertThat(toDos.get(0).getColor()).isEqualTo(findToDo.getColor());
        softAssertions.assertThat(toDos.get(0).getIsDone()).isEqualTo(findToDo.getIsDone());
        softAssertions.assertThat(toDos.get(0).getIsDone()).isEqualTo(findToDo.getIsDone());
        softAssertions.assertAll();
    }
}
