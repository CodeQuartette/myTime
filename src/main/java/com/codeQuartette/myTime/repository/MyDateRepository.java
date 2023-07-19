package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MyDateRepository extends JpaRepository<MyDate, Long> {

    Optional<MyDate> findByUserAndAndDate(User user, LocalDate date);
}
