package com.codeQuartette.myTime.controller;

import com.codeQuartette.myTime.auth.JwtProvider;
import com.codeQuartette.myTime.auth.TokenInfo;
import com.codeQuartette.myTime.controller.dto.UserDTO;
import com.codeQuartette.myTime.domain.User;
import com.codeQuartette.myTime.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.codeQuartette.myTime.auth.JwtProvider.BEARER;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("회원가입 API 테스트, 상태코드가 200이여야 한다.")
    void signup() throws Exception {
        UserDTO.Request request = UserDTO.Request.builder()
                .name("testUser")
                .nickname("testNickname")
                .email("testEmail@gmail.com")
                .birthday(LocalDate.of(2000, 2, 22))
                .password("password")
                .profileImage("http://ProfileImage.jpg")
                .gender(false)
                .build();

        mvc.perform(RestDocumentationRequestBuilders.post("/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("signup-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("birthday").type(JsonFieldType.STRING).description("유저 생년월일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저 패스워드"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                fieldWithPath("gender").type(JsonFieldType.BOOLEAN).description("유저 성별")
                        )));
    }

    @Test
    @DisplayName("로그인 API 테스트, 로그인 된 유저 정보와 토큰을 가져와야한다.")
    void login() throws Exception {
        UserDTO.Request request = UserDTO.Request.builder()
                .email("testEmail@gmail.com")
                .password("password")
                .build();

        UserDTO.Response response = UserDTO.Response.builder()
                .name("testUser")
                .nickname("testUserNickname")
                .email("testEmail@gmail.com")
                .birthday(LocalDate.of(2000, 2, 22))
                .profileImage("http://testUserProfileImage.jpg")
                .gender(false)
                .build();

        TokenInfo tokenInfo = TokenInfo.builder()
                .grantType(BEARER)
                .refreshToken("Bearer Refresh Token")
                .accessToken("Bearer Access Token")
                .build();

        response.setTokenInfo(tokenInfo);

        given(userService.login(any())).willReturn(response);

        mvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("testUser"))
                .andExpect(jsonPath("$.nickname").value("testUserNickname"))
                .andExpect(jsonPath("$.email").value("testEmail@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("2000-02-22"))
                .andExpect(jsonPath("$.profileImage").value("http://testUserProfileImage.jpg"))
                .andExpect(jsonPath("$.gender").value(false))
                .andExpect(jsonPath("$.tokenInfo.grantType").value(BEARER))
                .andExpect(jsonPath("$.tokenInfo.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.tokenInfo.accessToken", notNullValue()))
                .andDo(print())
                .andDo(document("login-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("birthday").type(JsonFieldType.STRING).description("유저 생년월일"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                fieldWithPath("gender").type(JsonFieldType.BOOLEAN).description("유저 성별"),
                                fieldWithPath("tokenInfo.grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("tokenInfo.refreshToken").type(JsonFieldType.STRING).description("재발급 토큰"),
                                fieldWithPath("tokenInfo.accessToken").type(JsonFieldType.STRING).description("인증 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃 API 테스트, 상태코드가 200이여야 한다.")
    void logout() throws Exception {
        String accessToken = "Bearer Access Token";

        mvc.perform(RestDocumentationRequestBuilders.get("/api/logout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("logout-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token")
                        )));
    }

    @Test
    @DisplayName("토큰 재발급 API 테스트, 토큰의 정보들이 일치해야 한다.")
    void reissueToken() throws Exception {
        String refreshToken = "Bearer Refresh Token";
        String accessToken = "Bearer Access Token";

        TokenInfo tokenInfo = TokenInfo.create(BEARER, refreshToken, accessToken);
        given(userService.reissueToken(anyString(), any())).willReturn(tokenInfo);

        mvc.perform(RestDocumentationRequestBuilders.get("/reissueToken")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Refresh_Token", refreshToken)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.grantType").value(BEARER))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print())
                .andDo(document("reissueToken-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName("Refresh_Token").description("Refresh Token"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("재발급 토큰"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("인증 토큰")
                        )));
    }

    @Test
    @DisplayName("회원정보 조회 API 테스트, 토큰에 해당하는 유저 정보를 가져와야 한다.")
    void getUser() throws Exception {
        String accessToken = "Bearer Access Token";
        User user = User.builder()
                .name("testName")
                .nickname("testNickname")
                .email("testEmail@gmail.com")
                .birthday(LocalDate.of(2000, 2, 22))
                .profileImage("http://ProfileImage.jpg")
                .gender(false)
                .build();

        given(userService.getUser(any())).willReturn(user);

        mvc.perform(RestDocumentationRequestBuilders.get("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.nickname").value("testNickname"))
                .andExpect(jsonPath("$.email").value("testEmail@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("2000-02-22"))
                .andExpect(jsonPath("$.profileImage").value("http://ProfileImage.jpg"))
                .andExpect(jsonPath("$.gender").value(false))
                .andDo(print())
                .andDo(document("get-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("birthday").type(JsonFieldType.STRING).description("2000-02-22"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("http://ProfileImage.jpg"),
                                fieldWithPath("gender").type(JsonFieldType.BOOLEAN).description(false)
                        )));
    }

    @Test
    @DisplayName("회원정보 수정 API 테스트, 변경 된 유저 정보가 일치해야 한다.")
    void updateUser() throws Exception {
        String accessToken = "Bearer Access Token";
        UserDTO.Request request = UserDTO.Request.builder()
                .name("newName")
                .nickname("newNickname")
                .password("password")
                .newPassword("newPassword")
                .build();

        User user = User.builder()
                .name("newName")
                .nickname("newNickname")
                .build();

        given(userService.updateUser(any(), any())).willReturn(user);

        mvc.perform(RestDocumentationRequestBuilders.patch("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.nickname").value("newNickname"))
                .andDo(print())
                .andDo(document("update-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("기존 비밀번호"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("변경 할 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("변결 할 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("변결 할 닉네임")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("변경 된 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경 된 닉네임")
                        )));
    }

    @Test
    @DisplayName("회원탈퇴 API 테스트, 해당 유저가 삭제되어야 한다.")
    void deleteUser() throws Exception {
        String accessToken = "Bearer Access Token";
        UserDTO.Request request = UserDTO.Request.builder()
                .email("testEmail@gmail.com")
                .password("password")
                .build();

        mvc.perform(RestDocumentationRequestBuilders.delete("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("delete-user",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Application/json"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("확인 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("확인 비밀번호")
                        )));
    }
}
