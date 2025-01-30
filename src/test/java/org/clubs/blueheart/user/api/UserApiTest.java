package org.clubs.blueheart.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.application.UserService;
import org.clubs.blueheart.user.dto.request.UserDeleteRequestDto;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
import org.clubs.blueheart.user.dto.request.UserUpdateRequestDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserApi 컨트롤러에 대한 통합 테스트 클래스
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockitoBean
    private UserService userService;

    private String token;

    @BeforeEach
    void setup() {
        // 테스트 시작 전에 유효한 JWT 토큰 생성
        token = createLoginJwt(1L, "201234567", "홍길동", UserRole.USER);
    }

    /**
     * JwtGenerator를 사용하여 테스트용 JWT 토큰을 생성하는 유틸 메서드
     */
    public String createLoginJwt(Long userId, String studentNumber, String username, UserRole role) {
        return jwtGenerator.createToken(
                Map.of(
                        "userId", userId,
                        "studentNumber", studentNumber,
                        "username", username,
                        "role", role.name()
                ),
                24L * 60 * 60 * 1000  // 1일 유효
        );
    }

    /**
     * 1. 사용자 생성 테스트
     */
    @Test
    @DisplayName("사용자 생성 성공 테스트 (인증 필요)")
    void createUser_ShouldReturn201() throws Exception {
        // Given
        UserInfoRequestDto requestDto = UserInfoRequestDto.builder()
                .studentNumber("201234567")
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(userService).createUser(any(UserInfoRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/create")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.USER_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.USER_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-1. 사용자 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("사용자 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void createUser_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: studentNumber가 빈 문자열인 경우
        UserInfoRequestDto requestDto = UserInfoRequestDto.builder()
                .studentNumber("")  // 유효하지 않은 studentNumber
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/create")
                .header("Authorization", "Bearer " + token)
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
     * 1-2. 사용자 생성 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("사용자 생성 실패 테스트: 인증 실패 (401 Unauthorized)")
    void createUser_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        UserInfoRequestDto requestDto = UserInfoRequestDto.builder()
                .studentNumber("201234567")
                .username("홍길동")
                .role(UserRole.USER)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/create")
                .header("Authorization", "Bearer invalid_token")  // 유효하지 않은 토큰
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
     * 2. 사용자 검색 테스트
     */
    @Test
    @DisplayName("키워드로 사용자 검색 성공 테스트 (인증 필요)")
    void findUserByKeyword_ShouldReturn200() throws Exception {
        // Given
        String keyword = "홍";

        List<UserInfoResponseDto> mockUsers = List.of(
                UserInfoResponseDto.builder()
                        .studentNumber("201234567")
                        .username("홍길동")
                        .role(UserRole.USER)
                        .build(),
                UserInfoResponseDto.builder()
                        .studentNumber("201234568")
                        .username("홍길순")
                        .role(UserRole.ADMIN)
                        .build()
        );

        Mockito.when(userService.findUserByKeyword(ArgumentMatchers.anyString()))
                .thenReturn(mockUsers);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/search/" + keyword)
                .header("Authorization", "Bearer " + token));  // 토큰 첨부

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.USER_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.USER_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockUsers.size()))
                .andExpect(jsonPath("$.data[0].studentNumber").value("201234567"))
                .andExpect(jsonPath("$.data[0].username").value("홍길동"))
                .andExpect(jsonPath("$.data[0].role").value("USER"))
                .andExpect(jsonPath("$.data[1].studentNumber").value("201234568"))
                .andExpect(jsonPath("$.data[1].username").value("홍길순"))
                .andExpect(jsonPath("$.data[1].role").value("ADMIN"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-1. 사용자 검색 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("사용자 검색 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void findUserByKeyword_NotFound_ShouldReturn404() throws Exception {
        // Given
        String keyword = "없는사용자";

        Mockito.when(userService.findUserByKeyword(ArgumentMatchers.anyString()))
                .thenThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/search/" + keyword)
                .header("Authorization", "Bearer " + token));

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
     * 3. 사용자 정보 업데이트 테스트
     */
    @Test
    @DisplayName("사용자 정보 업데이트 성공 테스트 (인증 필요)")
    void updateUser_ShouldReturn200() throws Exception {
        // Given
        UserUpdateRequestDto updateRequestDto = UserUpdateRequestDto.builder()
                .userId(1L)
                .username("홍길동")
                .studentNumber("201234567")
                .role(UserRole.ADMIN)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(userService).updateUserById(any(UserUpdateRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/update")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.USER_UPDATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.USER_UPDATED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-1. 사용자 정보 업데이트 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("사용자 정보 업데이트 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void updateUser_WithNonExistentUser_ShouldReturn404() throws Exception {
        // Given
        UserUpdateRequestDto updateRequestDto = UserUpdateRequestDto.builder()
                .userId(999L)  // 존재하지 않는 사용자 ID
                .username("김철수")
                .studentNumber("201234567")
                .role(UserRole.ADMIN)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        Mockito.doThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER))
                .when(userService).updateUserById(Mockito.any(UserUpdateRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/update")
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
     * 4. 사용자 삭제 테스트
     */
    @Test
    @DisplayName("사용자 삭제 성공 테스트 (인증 필요)")
    void deleteUser_ShouldReturn200() throws Exception {
        // Given
        UserDeleteRequestDto requestDto = UserDeleteRequestDto.builder()
                .userId(1L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(userService).deleteUserById(any(UserDeleteRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/delete")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.USER_DELETED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.USER_DELETED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-1. 사용자 삭제 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("사용자 삭제 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void deleteUser_WithNonExistentUser_ShouldReturn404() throws Exception {
        // Given
        UserDeleteRequestDto requestDto = UserDeleteRequestDto.builder()
                .userId(999L)  // 존재하지 않는 사용자 ID
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        Mockito.doThrow(new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER))
                .when(userService).deleteUserById(Mockito.any(UserDeleteRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/delete")
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
     * 5. 모든 사용자 조회 테스트
     */
    @Test
    @DisplayName("모든 사용자 조회 성공 테스트 (인증 필요)")
    void findAllUser_ShouldReturn200() throws Exception {
        // Given
        List<UserInfoResponseDto> mockUsers = List.of(
                UserInfoResponseDto.builder()
                        .studentNumber("201234567")
                        .username("홍길동")
                        .role(UserRole.USER)
                        .build(),
                UserInfoResponseDto.builder()
                        .studentNumber("201234568")
                        .username("김철수")
                        .role(UserRole.ADMIN)
                        .build()
        );

        Mockito.when(userService.findAllUser()).thenReturn(mockUsers);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/all")
                .header("Authorization", "Bearer " + token));  // 토큰 첨부

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.USER_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.USER_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockUsers.size()))
                .andExpect(jsonPath("$.data[0].studentNumber").value("201234567"))
                .andExpect(jsonPath("$.data[0].username").value("홍길동"))
                .andExpect(jsonPath("$.data[0].role").value("USER"))
                .andExpect(jsonPath("$.data[1].studentNumber").value("201234568"))
                .andExpect(jsonPath("$.data[1].username").value("김철수"))
                .andExpect(jsonPath("$.data[1].role").value("ADMIN"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5-1. 모든 사용자 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("모든 사용자 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void findAllUser_WithInvalidToken_ShouldReturn401() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/all")
                .header("Authorization", "Bearer invalid_token"));  // 유효하지 않은 토큰

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

}
