package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @InjectSoftAssertions
    private SoftAssertions softly;

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
                .andExpect(jsonPath("$.gender").value("false"))
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        String url = "http://localhost:" + port + "/user";

        MockHttpServletResponse response = mvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map responseMap = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Map.class);

        // SoftAssertions를 사용하면 통과되지 못한 테스트를 모두 보여준다.
        softly.assertThat(responseMap.get("name")).isEqualTo("정인호");
        softly.assertThat(responseMap.get("nickname")).isEqualTo("이노");
        softly.assertThat(responseMap.get("email")).isEqualTo("enolj76@gmail.com");
        softly.assertThat(responseMap.get("birthday")).isEqualTo("1996-02-27");
        softly.assertThat(responseMap.get("profileImage")).isEqualTo("/image/eno.jpg");
        softly.assertThat(responseMap.get("gender")).isEqualTo(false);
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        String url = "http://localhost:" + port + "/user";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .password("1234")
                .newPassword("5678")
                .nickname("eno")
                .name("정호인")
                .build();

        mvc
                .perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE")
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정호인"))
                .andExpect(jsonPath("$.nickname").value("eno"))
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        String url = "http://localhost:" + port + "/user";
        UserDTO.Request userDTO = UserDTO.Request.builder()
                .email("enolj76@gmail.com")
                .password("1234")
                .build();

         mvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb2RlUXVhcnRldHRlIiwic3ViIjoiZW5vbGo3NkBnbWFpbC5jb20iLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.ep-gj8dGemncg_NxgxdwZ3plLjnDX7gG31NZsM4MTOE")
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
