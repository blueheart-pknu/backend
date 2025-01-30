package org.clubs.blueheart.notification.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.notification.application.NotificationService;
import org.clubs.blueheart.notification.dto.request.NotificationRequestDto;
import org.clubs.blueheart.notification.dto.response.NotificationResponseDto;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NotificationApi 컨트롤러에 대한 통합 테스트 클래스
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockitoBean
    private NotificationService notificationService;

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
     * 1. 액티비티에 대한 알림 전송 테스트
     */
    @Test
    @DisplayName("액티비티에 대한 알림 전송 성공 테스트 (인증 필요)")
    void notificationActivity_ShouldReturn201() throws Exception {
        // Given
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("Hello World1")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(notificationService).notificationActivity(any(NotificationRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/activity")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.NOTIFICATION_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.NOTIFICATION_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-1. 액티비티 알림 전송 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("액티비티 알림 전송 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void notificationActivity_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: content가 빈 문자열인 경우
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("")  // 유효하지 않은 content
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/activity")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("content: Content must not be blank"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-2. 액티비티 알림 전송 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 알림 전송 실패 테스트: 인증 실패 (401 Unauthorized)")
    void notificationActivity_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("Hello World")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/activity")
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
     * 2. 그룹에 대한 알림 전송 테스트
     */
    @Test
    @DisplayName("그룹에 대한 알림 전송 성공 테스트 (인증 필요)")
    void notificationGroup_ShouldReturn201() throws Exception {
        // Given
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("Hello World1")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(notificationService).notificationGroup(any(NotificationRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/group")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.NOTIFICATION_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.NOTIFICATION_CREATED.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-1. 그룹 알림 전송 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("그룹 알림 전송 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void notificationGroup_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: receiverId가 null인 경우
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(null)  // 유효하지 않은 receiverId
                .content("Hello Group")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/group")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("receiverId: Receiver ID must not be null"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-2. 그룹 알림 전송 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("그룹 알림 전송 실패 테스트: 인증 실패 (401 Unauthorized)")
    void notificationGroup_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        NotificationRequestDto requestDto = NotificationRequestDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("Hello Group")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(post("/api/v1/notification/group")
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
     * 3. 현재 사용자에 대한 모든 알림 조회 테스트
     */
    @Test
    @DisplayName("현재 사용자의 모든 알림 조회 성공 테스트 (인증 필요)")
    void findAllNotificationMe_ShouldReturn200() throws Exception {
        // Given
        List<NotificationResponseDto> mockNotifications = List.of(
                NotificationResponseDto.builder()
                        .senderUsername("User 1")
                        .content("Your group has been updated.")
                        .createdAt(LocalDateTime.now())
                        .build(),
                NotificationResponseDto.builder()
                        .senderUsername("User 2")
                        .content("New activity available.")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        Mockito.when(notificationService.findAllNotificationMe(anyLong()))
                .thenReturn(mockNotifications);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/notification/me")
                .header("Authorization", "Bearer " + token));  // 토큰 첨부

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.NOTIFICATION_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.NOTIFICATION_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockNotifications.size()))
                .andExpect(jsonPath("$.data[0].senderUsername").value("User 1"))
                .andExpect(jsonPath("$.data[0].content").value("Your group has been updated."))
                .andExpect(jsonPath("$.data[1].senderUsername").value("User 2"))
                .andExpect(jsonPath("$.data[1].content").value("New activity available."))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-1. 현재 사용자의 모든 알림 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("현재 사용자의 모든 알림 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void findAllNotificationMe_WithInvalidToken_ShouldReturn401() throws Exception {
        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(get("/api/v1/notification/me")
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

    /**
     * 3-2. 현재 사용자의 모든 알림 조회 실패 테스트: 알림 없음 (404 Not Found)
     */
    @Test
    @DisplayName("현재 사용자의 모든 알림 조회 실패 테스트: 알림 없음 (404 Not Found)")
    void findAllNotificationMe_NoNotifications_ShouldReturn404() throws Exception {
        // Mocking the service to throw exception
        Mockito.when(notificationService.findAllNotificationMe(anyLong()))
                .thenThrow(new RepositoryException(ExceptionStatus.NOTIFICATION_NOT_FOUND));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/notification/me")
                .header("Authorization", "Bearer " + token));  // 토큰 첨부

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.NOTIFICATION_NOT_FOUND.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.NOTIFICATION_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

}
