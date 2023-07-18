package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.MyDate;
import com.codeQuartette.myTime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyDateRepository extends JpaRepository<MyDate, Long> {

    public List<MyDate> findAllByUser(User user);
}
