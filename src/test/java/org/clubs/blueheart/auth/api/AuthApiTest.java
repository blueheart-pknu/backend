package org.clubs.blueheart.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.auth.application.AuthService;
import org.clubs.blueheart.auth.dto.request.AuthInviteAllRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthInviteOneRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthVerifyRequestDto;
import org.clubs.blueheart.auth.dto.response.AuthJwtResponseDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private String token;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @BeforeEach
    void setup() {
        // 테스트 시작 전에 유효한 JWT 토큰 생성
        token = createLoginJwt(1L, "201234567", "홍길동", UserRole.USER);
    }

    /**
     * JwtProvider를 사용하여 테스트용 JWT 토큰을 생성하는 유틸 메서드
     */
    public String createLoginJwt(Long userId, String studentNumber, String username, UserRole role) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("studentNumber", studentNumber);
        payload.put("username", username);
        payload.put("role", role);

        // 예: 최종 JWT는 1일 유효
        long finalExpire = 24L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, finalExpire);
    }

    /**
     * 1. 사용자 등록 테스트
     */
    @Test
    @DisplayName("사용자 등록 성공 테스트 (인증 필요 없음)")
    void registerUser_ShouldReturn201() throws Exception {
        // Given
        UserInfoRequestDto userInfoRequestDto = UserInfoRequestDto.builder()
                .studentNumber("201234567")
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        String requestBody = objectMapper.writeValueAsString(userInfoRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(authService).registerUser(any(UserInfoRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_USER_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_USER_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-1. 사용자 등록 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("사용자 등록 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void registerUser_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: studentNumber가 빈 문자열인 경우
        UserInfoRequestDto userInfoRequestDto = UserInfoRequestDto.builder()
                .studentNumber("")  // 유효하지 않은 studentNumber
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        String requestBody = objectMapper.writeValueAsString(userInfoRequestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("studentNumber: Student number must not be blank"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 2. 사용자 로그인 테스트
     */
    @Test
    @DisplayName("사용자 로그인 성공 테스트 (인증 필요 없음)")
    void loginUser_ShouldReturn200() throws Exception {
        // Given
        AuthLoginRequestDto authLoginRequestDto = AuthLoginRequestDto.builder()
                .studentNumber("201234567")
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(authLoginRequestDto);

        // Mocking the service methods
        // isSessionValid가 boolean을 반환한다고 가정
        Mockito.when(authService.isSessionValid(anyString())).thenReturn(true);

        AuthJwtResponseDto mockAuthJwtResponseDto = AuthJwtResponseDto.builder()
                .id(1L)
                .studentNumber("201234567")
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        Mockito.when(authService.loginUserByStudentNumberAndUsername(any(AuthLoginRequestDto.class)))
                .thenReturn(mockAuthJwtResponseDto);

        Mockito.when(authService.createLoginJwt(anyLong(), anyString(), anyString(), any(UserRole.class)))
                .thenReturn("mocked-jwt-token");

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_LOGGED_IN.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_LOGGED_IN.getMessage()))
                .andExpect(jsonPath("$.data").value(UserRole.USER.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-1. 사용자 로그인 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("사용자 로그인 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void loginUser_NotFound_ShouldReturn404() throws Exception {
        // Given
        AuthLoginRequestDto authLoginRequestDto = AuthLoginRequestDto.builder()
                .studentNumber("201299999")
                .username("존재하지않음")
                .build();

        String requestBody = objectMapper.writeValueAsString(authLoginRequestDto);

        Mockito.when(authService.loginUserByStudentNumberAndUsername(any(AuthLoginRequestDto.class)))
                .thenThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.USER_NOT_FOUND_USER.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.USER_NOT_FOUND_USER.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 3. 사용자 로그아웃 테스트
     */
    @Test
    @DisplayName("사용자 로그아웃 성공 테스트 (인증 필요)")
    void logoutUser_ShouldReturn200() throws Exception {
        // Given
        AuthLoginRequestDto authLoginRequestDto = AuthLoginRequestDto.builder()
                .studentNumber("201234567")
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(authLoginRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(authService).logoutUser(any(AuthLoginRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_LOGGED_OUT.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_LOGGED_OUT.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-1. 사용자 로그아웃 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("사용자 로그아웃 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void logoutUser_NotFound_ShouldReturn404() throws Exception {
        // Given
        AuthLoginRequestDto authLoginRequestDto = AuthLoginRequestDto.builder()
                .studentNumber("201299999")
                .username("존재하지않음")
                .build();

        String requestBody = objectMapper.writeValueAsString(authLoginRequestDto);

        Mockito.doThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER))
                .when(authService).logoutUser(any(AuthLoginRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));



        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.USER_NOT_FOUND_USER.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.USER_NOT_FOUND_USER.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 4. 전체 사용자 초대 테스트
     */
    @Test
    @DisplayName("전체 사용자 초대 성공 테스트 (인증 필요)")
    void inviteAllUser_ShouldReturn201() throws Exception {
        // Given
        AuthInviteAllRequestDto authInviteAllRequestDto = AuthInviteAllRequestDto.builder()
                .creatorId(1L)
                .build();

        String requestBody = objectMapper.writeValueAsString(authInviteAllRequestDto);

        String mockInviteAllUrl = "http://invite-all.example.com";

        Mockito.when(authService.inviteAllUser(any(AuthInviteAllRequestDto.class)))
                .thenReturn(mockInviteAllUrl);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/invite/all")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_INVITE_LINK_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_INVITE_LINK_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").value(mockInviteAllUrl))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-1. 전체 사용자 초대 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("전체 사용자 초대 실패 테스트: 인증 실패 (401 Unauthorized)")
    void inviteAllUser_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        AuthInviteAllRequestDto authInviteAllRequestDto = AuthInviteAllRequestDto.builder()
                .creatorId(1L)
                .build();

        String requestBody = objectMapper.writeValueAsString(authInviteAllRequestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/invite/all")
                .header("Authorization", "Bearer invalid_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 5. 특정 사용자 초대 테스트
     */
    @Test
    @DisplayName("특정 사용자 초대 성공 테스트 (인증 필요)")
    void inviteUserByStudentNumber_ShouldReturn201() throws Exception {
        // Given
        AuthInviteOneRequestDto authInviteOneRequestDto = AuthInviteOneRequestDto.builder()
                .creatorId(1L)
                .targetStudentNumber("201234568")
                .targetUsername("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(authInviteOneRequestDto);

        String mockInviteUrl = "http://invite-one.example.com";

        Mockito.when(authService.inviteUserByStudentNumber(any(AuthInviteOneRequestDto.class)))
                .thenReturn(mockInviteUrl);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/invite")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_INVITE_LINK_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_INVITE_LINK_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").value(mockInviteUrl))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5-1. 특정 사용자 초대 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("특정 사용자 초대 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void inviteUserByStudentNumber_NotFound_ShouldReturn404() throws Exception {
        // Given
        AuthInviteOneRequestDto authInviteOneRequestDto = AuthInviteOneRequestDto.builder()
                .creatorId(1L)
                .targetStudentNumber("201299999")
                .targetUsername("존재하지않음")
                .build();

        String requestBody = objectMapper.writeValueAsString(authInviteOneRequestDto);

        Mockito.when(authService.inviteUserByStudentNumber(any(AuthInviteOneRequestDto.class)))
                .thenThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/invite")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.USER_NOT_FOUND_USER.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.USER_NOT_FOUND_USER.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 6. 초대 코드 검증 테스트
     */
    @Test
    @DisplayName("초대 코드 검증 성공 테스트 (인증 필요 없음)")
    void verifyInviteCode_ShouldReturn200() throws Exception {
        // Given
        AuthVerifyRequestDto authVerifyRequestDto = AuthVerifyRequestDto.builder()
                .code("VALID_INVITE_CODE")
                .build();

        String requestBody = objectMapper.writeValueAsString(authVerifyRequestDto);

        String mockSessionId = "mocked-session-id";

        Mockito.when(authService.verifyInviteCode(any(AuthVerifyRequestDto.class)))
                .thenReturn(mockSessionId);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.AUTH_VERIFIED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.AUTH_VERIFIED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andExpect(cookie().value("SESSION_ID", mockSessionId));
    }

    /**
     * 6-1. 초대 코드 검증 실패 테스트: 잘못된 코드 (400 Bad Request)
     */
    @Test
    @DisplayName("초대 코드 검증 실패 테스트: 잘못된 코드 (400 Bad Request)")
    void verifyInviteCode_WithInvalidCode_ShouldReturn400() throws Exception {
        // Given
        AuthVerifyRequestDto authVerifyRequestDto = AuthVerifyRequestDto.builder()
                .code("INVALID_CODE")
                .build();

        String requestBody = objectMapper.writeValueAsString(authVerifyRequestDto);

        Mockito.when(authService.verifyInviteCode(any(AuthVerifyRequestDto.class)))
                .thenThrow(new RepositoryException(ExceptionStatus.AUTH_INVALID_INVITE_CODE));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_INVALID_INVITE_CODE.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_INVALID_INVITE_CODE.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

}
