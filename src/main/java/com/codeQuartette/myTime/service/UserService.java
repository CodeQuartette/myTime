package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.User;

public interface UserService {
    void testUser();

    User findById(Long userId);

}
