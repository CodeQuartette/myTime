package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.exception.ToDoNotFoundException;
import com.codeQuartette.myTime.repository.ToDoRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class ToDoServiceImplTest {

    @Autowired
    private ToDoServiceImpl toDoService;

    @Autowired
    private ToDoRepository toDoRepository;

    ToDo findTodo(Long todoId) {
        return toDoRepository.findById(todoId)
                .orElseThrow(ToDoNotFoundException::new);
    }

    @Test
    @DisplayName("할 일 등록")
    void create() {
        Long userId = 1L;

        ToDoDTO.Request request = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        ToDo toDo = toDoService.create(userId, request);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(findTodo(toDo.getId()).getId()).isEqualTo(toDo.getId());
        softAssertions.assertThat(findTodo(toDo.getId()).getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    @DisplayName("할 일 수정")
    void update() {
        Long userId = 1L;

        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(findTodo(toDo.getId()).getId()).isEqualTo(toDo.getId());


        ToDoDTO.Request update = ToDoDTO.Request.builder()
                .title("방 없애기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 11))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        ToDo toDoUpdate = toDoService.update(userId, toDo.getId(), update);

        softAssertions.assertThat(toDo.getId()).isEqualTo(toDoUpdate.getId());
    }

    @Test
    @DisplayName("할 일 삭제")
    void delete() {
        Long userId = 1L;
        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.TRUE)
                .isBlind(Boolean.FALSE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(findTodo(toDo.getId()).getId()).isEqualTo(toDo.getId());
        softAssertions.assertThat(findTodo(toDo.getId()).getTitle()).isEqualTo(create.getTitle());

        toDoService.delete(userId, toDo.getId());
        toDo = toDoRepository.findById(toDo.getId()).orElse(null);
        softAssertions.assertThat(toDo).isNull();
    }
}
