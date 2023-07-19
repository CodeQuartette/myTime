package com.codeQuartette.myTime.service.impl;

import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.repository.UserRepository;
import com.codeQuartette.myTime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void testUser() {
        User tree = userRepository.findById(1L).orElseThrow(
                () -> new NoSuchElementException("트리 없다.")
        );

        System.out.println("tree.getNickname() = " + tree.getNickname());
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 아이디가 없습니다"));
    }
}
