package com.codeQuartette.myTime.repository;

import com.codeQuartette.myTime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
