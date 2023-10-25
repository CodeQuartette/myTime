package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findAllByMyDate(MyDate myDate);
}
