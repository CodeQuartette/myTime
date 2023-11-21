package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.controller.dto.ToDoDTO;
import com.codeQuartette.myTime.domain.ToDo;
import com.codeQuartette.myTime.domain.value.Color;
import com.codeQuartette.myTime.repository.ToDoRepository;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
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

    @InjectSoftAssertions
    private SoftAssertions softly;

    @Test
    @DisplayName("할 일 조회 로직 테스트, 해당 할 일 정보를 조회해야 한다.")
    void find() {

        //given
        Long userId = 1L;
        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        toDoService.create(userId, create);

        //when
        ToDo findToDo = toDoRepository.findById(userId).get();
        List<ToDo> toDos = toDoService.find(userId, findToDo.getId());

        //then
        softly.assertThat(toDos.get(0).getTitle()).isEqualTo(findToDo.getTitle());
        softly.assertThat(toDos.get(0).getColor()).isEqualTo(findToDo.getColor());
        softly.assertThat(toDos.get(0).getDate()).isEqualTo(findToDo.getDate());
        softly.assertThat(toDos.get(0).getIsDone()).isEqualTo(findToDo.getIsDone());
        softly.assertThat(toDos.get(0).getIsBlind()).isEqualTo(findToDo.getIsBlind());
    }

    @Test
    @DisplayName("할 일 생성 로직 테스트, 할 일 정보가 DB에 저장되어야 한다.")
    void create() {

        //given
        Long userId = 1L;

        ToDoDTO.Request request = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        toDoService.create(userId, request);

        // when
        ToDo toDo = toDoRepository.findById(userId).get();

        // then
        softly.assertThat(toDo.getTitle()).isEqualTo(request.getTitle());
        softly.assertThat(toDo.getColor()).isEqualTo(request.getColor());
        softly.assertThat(toDo.getDate()).isEqualTo(request.getDate());
        softly.assertThat(toDo.getIsDone()).isEqualTo(request.getIsDone());
        softly.assertThat(toDo.getIsBlind()).isEqualTo(request.getIsBlind());
    }

    @Test
    @DisplayName("할 일 수정 로직 테스트, 수정한 데이터를 DB에 반영해야 한다")
    void update() {

        //given
        Long userId = 1L;

        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        //when
        ToDoDTO.Request update = ToDoDTO.Request.builder()
                .title("방 없애기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 11))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDoUpdated = toDoService.update(userId, toDo.getId(), update);

        //then
        softly.assertThat(toDoUpdated.getId()).isEqualTo(toDo.getId());
        softly.assertThat(toDoUpdated.getTitle()).isEqualTo(update.getTitle());
        softly.assertThat(toDoUpdated.getColor()).isEqualTo(update.getColor());
        softly.assertThat(toDoUpdated.getDate()).isEqualTo(update.getDate());
        softly.assertThat(toDoUpdated.getIsDone()).isEqualTo(update.getIsDone());
        softly.assertThat(toDoUpdated.getIsBlind()).isEqualTo(update.getIsBlind());
    }

    @Test
    @DisplayName("할 일 삭제 로직 테스트, 할 일 정보를 DB에서 삭제해야 한다. ")
    void delete() {

        //given
        Long userId = 1L;
        ToDoDTO.Request create = ToDoDTO.Request.builder()
                .title("방 만들기")
                .color(Color.FFADAD)
                .date(LocalDate.of(2023, 11, 2))
                .isDone(Boolean.FALSE)
                .isBlind(Boolean.TRUE)
                .build();

        ToDo toDo = toDoService.create(userId, create);

        //when
        toDoService.delete(userId, toDo.getId());

        //then
        toDo = toDoRepository.findById(toDo.getId()).orElse(null);
        softly.assertThat(toDo).isNull();
    }
}
