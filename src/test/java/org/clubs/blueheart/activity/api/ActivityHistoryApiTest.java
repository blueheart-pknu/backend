package org.clubs.blueheart.activity.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.activity.application.ActivityHistoryService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivitySubscribeRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 전체 컨텍스트 로딩 & 보안 필터 적용
@SpringBootTest
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
class ActivityHistoryApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockBean
    private ActivityHistoryService activityHistoryService;

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
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("studentNumber", studentNumber);
        payload.put("username", username);
        payload.put("role", role);

        // 예: 최종 JWT는 1일 유효
        long finalExpire = 24L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, finalExpire);
    }

    @Test
    @DisplayName("빌더로 생성한 ActivitySubscribeRequestDto로 액티비티 구독 테스트 (인증 사용)")
    void subscribeActivity_ShouldReturn200() throws Exception {
        // Given
        ActivitySubscribeRequestDto subscribeRequestDto = ActivitySubscribeRequestDto.builder()
                .activityId(1L)
                .userId(1L)
                .build();

        String requestBody = objectMapper.writeValueAsString(subscribeRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(activityHistoryService).subscribeActivity(Mockito.any(ActivitySubscribeRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/activity-history/subscribe")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_HISTORY_SUBSCRIBED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_HISTORY_SUBSCRIBED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("빌더로 생성한 ActivitySubscribeRequestDto로 액티비티 구독 해지 테스트 (인증 사용)")
    void unsubscribeActivity_ShouldReturn200() throws Exception {
        // Given
        ActivitySubscribeRequestDto unsubscribeRequestDto = ActivitySubscribeRequestDto.builder()
                .activityId(1L)
                .userId(1L)
                .build();

        String requestBody = objectMapper.writeValueAsString(unsubscribeRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(activityHistoryService).unsubscribeActivity(Mockito.any(ActivitySubscribeRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/activity-history/unsubscribe")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_HISTORY_UNSUBSCRIBED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_HISTORY_UNSUBSCRIBED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("내 활동 이력을 조회합니다 (인증 사용)")
    void getMyActivityHistoryInfo_ShouldReturn200() throws Exception {
        // Given
        Long userId = 1L;
        List<ActivitySearchResponseDto> mockActivityHistory = List.of(
                ActivitySearchResponseDto.builder()
                        .activityId(1L)
                        .title("Activity 1")
                        .status(ActivityStatus.PROGRESSING)
                        .isSubscribed(true)
                        .place("Seoul")
                        .currentNumber(5)
                        .maxNumber(10)
                        .expiredAt(LocalDateTime.now().plusDays(1))
                        .build(),
                ActivitySearchResponseDto.builder()
                        .activityId(2L)
                        .title("Activity 2")
                        .status(ActivityStatus.COMPLETED)
                        .isSubscribed(false)
                        .place("Busan")
                        .currentNumber(10)
                        .maxNumber(10)
                        .expiredAt(LocalDateTime.now().plusDays(3))
                        .build()
        );

        Mockito.when(activityHistoryService.getMyActivityHistoryInfo(userId)).thenReturn(mockActivityHistory);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity-history/me")
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockActivityHistory.size()))
                .andExpect(jsonPath("$.data[0].activityId").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Activity 1"))
                .andExpect(jsonPath("$.data[1].activityId").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Activity 2"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("특정 활동에 구독한 사용자를 조회합니다 (인증 사용)")
    void findSubscribedUser_ShouldReturn200() throws Exception {
        // Given
        Long activityId = 1L;
        List<UserInfoResponseDto> mockSubscribedUsers = List.of(
                UserInfoResponseDto.builder()
                        .studentNumber("201234568")
                        .username("사용자1")
                        .role(UserRole.USER)
                        .build(),
                UserInfoResponseDto.builder()
                        .studentNumber("201234569")
                        .username("사용자2")
                        .role(UserRole.USER)
                        .build()
        );

        Mockito.when(activityHistoryService.findSubscribedUser(activityId)).thenReturn(mockSubscribedUsers);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity-history/" + activityId)
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockSubscribedUsers.size()))
                .andExpect(jsonPath("$.data[0].studentNumber").value("201234568"))
                .andExpect(jsonPath("$.data[0].username").value("사용자1"))
                .andExpect(jsonPath("$.data[0].role").value("USER"))
                .andExpect(jsonPath("$.data[1].studentNumber").value("201234569"))
                .andExpect(jsonPath("$.data[1].username").value("사용자2"))
                .andExpect(jsonPath("$.data[1].role").value("USER"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }
}
