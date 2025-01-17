package org.clubs.blueheart.activity.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.activity.application.ActivityService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.activity.dto.response.ActivitySearchResponseDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.config.jwt.JwtProvider;
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

// (B안) 전체 컨텍스트 로딩 & 보안 필터 적용
@SpringBootTest
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
class ActivityApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockBean
    private ActivityService activityService;

    // JwtCookieFilter를 MockBean으로 등록하지 말고, 실제 필터가 동작하도록 유지
    // @MockBean
    // private JwtCookieFilter jwtCookieFilter;

    private String token;

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

    @Test
    @DisplayName("빌더로 생성한 ActivityCreateRequestDto로 액티비티 생성 테스트 (인증 사용)")
    void createActivityWithBuilder_ShouldReturn201() throws Exception {
        // Given
        // mock service가 있다고 치고, 실제 db 저장은 생략
        ActivityCreateRequestDto requestDto = ActivityCreateRequestDto.builder()
                .creatorId(1L)
                .title("Sample Activity")
                .maxNumber(10)
                .description("Test description")
                .place("Seoul")
                .placeUrl("http://example.com")
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/activity/create")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_CREATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_CREATED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("특정 ID로 액티비티 세부 정보 조회 (인증 사용)")
    void findOneActivityDetailById_ShouldReturn200() throws Exception {
        // Given
        // mock service
        ActivityDetailResponseDto responseDto = ActivityDetailResponseDto.builder()
                .activityId(1L)
                .title("Sample Activity")
                .status(ActivityStatus.PROGRESSING)
                .isSubscribed(true)
                .place("Seoul")
                .currentNumber(3)
                .maxNumber(10)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .description("This is a sample activity description.")
                .placeUrl("http://example.com")
                .build();

        Mockito.when(activityService.findOneActivityDetailById(1L))
                .thenReturn(responseDto);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/detail/1")
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Sample Activity"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    // 그 외 updateActivity, findActivityByStatus, deleteActivity 등도
    // 동일하게 "Authorization" 헤더에 토큰을 넣어서 호출하면 됩니다.

    @Test
    @DisplayName("액티비티를 삭제합니다 (인증 사용)")
    void deleteActivity_ShouldReturn200() throws Exception {
        // Given
        ActivityDeleteRequestDto requestDto = ActivityDeleteRequestDto.builder()
                .activityId(1L)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/activity/delete")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_DELETED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_DELETED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("빌더로 생성한 ActivityUpdateRequestDto로 액티비티 업데이트 테스트 (인증 사용)")
    void updateActivity_ShouldReturn200() throws Exception {
        // Given
        ActivityUpdateRequestDto updateRequestDto = ActivityUpdateRequestDto.builder()
                .activityId(1L)
                .title("Updated Activity Title")
                .status(ActivityStatus.DONE)
                .maxNumber(15)
                .description("Updated description")
                .place("Busan")
                .placeUrl("http://updated-example.com")
                .expiredAt(LocalDateTime.now().plusDays(2))
                .build();

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        // Mocking the service method
        Mockito.doNothing().when(activityService).updateActivity(Mockito.any(ActivityUpdateRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/activity/update")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_UPDATED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_UPDATED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("특정 상태의 액티비티를 조회합니다 (인증 사용)")
    void findActivityByStatus_ShouldReturn200() throws Exception {
        // Given
        ActivityStatus status = ActivityStatus.PROGRESSING;

        List<ActivitySearchResponseDto> mockActivities = List.of(
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
                        .status(ActivityStatus.PROGRESSING)
                        .isSubscribed(false)
                        .place("Busan")
                        .currentNumber(3)
                        .maxNumber(10)
                        .expiredAt(LocalDateTime.now().plusDays(3))
                        .build()
        );

        Mockito.when(activityService.findActivityByStatus(status)).thenReturn(mockActivities);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/" + status.name())
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockActivities.size()))
                .andExpect(jsonPath("$.data[0].activityId").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Activity 1"))
                .andExpect(jsonPath("$.data[1].activityId").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Activity 2"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("모든 액티비티를 조회합니다 (인증 사용)")
    void findAllActivity_ShouldReturn200() throws Exception {
        // Given
        List<ActivitySearchResponseDto> mockActivities = List.of(
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

        Mockito.when(activityService.findAllActivity()).thenReturn(mockActivities);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/all")
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(ResponseStatus.ACTIVITY_SEARCHED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_SEARCHED.getMessage()))
                .andExpect(jsonPath("$.data.length()").value(mockActivities.size()))
                .andExpect(jsonPath("$.data[0].activityId").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Activity 1"))
                .andExpect(jsonPath("$.data[1].activityId").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("Activity 2"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }
}
