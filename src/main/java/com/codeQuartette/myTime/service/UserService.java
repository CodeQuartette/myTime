package com.codeQuartette.myTime.service;

import com.codeQuartette.myTime.domain.User;

public interface UserService {
    void testUser();

    public User findById(Long userId);
}
