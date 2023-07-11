package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.MyDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDateRepository extends JpaRepository<MyDate, Long> {
}
