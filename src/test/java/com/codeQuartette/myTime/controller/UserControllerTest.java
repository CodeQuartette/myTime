package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static com.codeQuartette.myTime.auth.JwtProvider.BEARER;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @LocalServerPort
    private int port;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Transactional
    @DisplayName("회원가입 API 테스트, 상태코드가 200이여야 한다.")
    void signup() throws Exception {
        String url = "http://localhost:" + port + "/signup";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .name("testUser")
                .nickname("testUserNickname")
                .email("testUser@gmail.com")
                .birthday(LocalDate.of(2000, 1,11))
                .password("password")
                .profileImage("http://testUserProfileImage.jpg")
                .gender(false)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mvc
                .perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("로그인 API 테스트, 로그인 된 유저 정보와 토큰을 가져와야한다.")
    void login() throws Exception {
        String url = "http://localhost:" + port + "/login";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();

        mvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정인호"))
                .andExpect(jsonPath("$.nickname").value("이노"))
                .andExpect(jsonPath("$.email").value("enolj76@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1996-02-27"))
                .andExpect(jsonPath("$.profileImage").value("/image/eno.jpg"))
                .andExpect(jsonPath("$.gender").value(false))
                .andExpect(jsonPath("$.tokenInfo.grantType").value(BEARER))
                .andExpect(jsonPath("$.tokenInfo.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.tokenInfo.accessToken", notNullValue()));
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 API 테스트, 토큰의 정보들이 일치해야 한다.")
    void reissueToken() throws Exception {
        String url = "http://localhost:" + port + "/reissueToken";
        String refreshToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIn0.ysfQimdEO_LZwRgZEEPDI0dxQKlvnIXSWQgpZHnJqRg";
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE";

        mvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Refresh_Token", refreshToken)
                        .header(HttpHeaders.AUTHORIZATION,accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value(BEARER))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()));
    }

    @Test
    @Transactional
    @DisplayName("회원정보 조회 API 테스트, 토큰에 해당하는 유저 정보를 가져와야 한다.")
    void getUser() throws Exception {
        String url = "http://localhost:" + port + "/user";
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE";

        mvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정인호"))
                .andExpect(jsonPath("$.nickname").value("이노"))
                .andExpect(jsonPath("$.email").value("enolj76@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1996-02-27"))
                .andExpect(jsonPath("$.profileImage").value("/image/eno.jpg"))
                .andExpect(jsonPath("$.gender").value(false));
    }

    @Test
    @Transactional
    @DisplayName("회원정보 수정 API 테스트, 변경 된 유저 정보가 일치해야 한다.")
    void updateUser() throws Exception {
        String url = "http://localhost:" + port + "/user";
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .password("1234")
                .newPassword("5678")
                .nickname("eno")
                .name("정호인")
                .build();

        mvc
                .perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,accessToken)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정호인"))
                .andExpect(jsonPath("$.nickname").value("eno"))
                .andExpect(jsonPath("$.tokenInfo", nullValue()));
    }

    @Test
    @Transactional
    @DisplayName("회원탈퇴 API 테스트, 해당 유저가 삭제되어야 한다.")
    void deleteUser() throws Exception {
        String url = "http://localhost:" + port + "/user";
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();

         mvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,accessToken)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
