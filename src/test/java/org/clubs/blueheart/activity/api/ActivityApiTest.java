package org.clubs.blueheart.activity.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.activity.application.ActivityService;
import org.clubs.blueheart.activity.domain.ActivityStatus;
import org.clubs.blueheart.activity.dto.request.ActivityCreateRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityDeleteRequestDto;
import org.clubs.blueheart.activity.dto.request.ActivityUpdateRequestDto;
import org.clubs.blueheart.activity.dto.response.ActivityDetailResponseDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ActivityApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockitoBean
    private ActivityService activityService;

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

    /**
     * 1. 액티비티 생성 성공 테스트
     */
    @Test
    @DisplayName("빌더로 생성한 ActivityCreateRequestDto로 액티비티 생성 성공 테스트 (인증 사용)")
    void createActivityWithBuilder_ShouldReturn201() throws Exception {
        // Given
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

        // Mocking the service method
        Mockito.doNothing().when(activityService).createActivity(any(ActivityCreateRequestDto.class));

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

    /**
     * 1-1. 액티비티 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("액티비티 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void createActivity_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: title이 빈 문자열인 경우
        ActivityCreateRequestDto requestDto = ActivityCreateRequestDto.builder()
                .creatorId(1L)
                .title("")  // 유효하지 않은 title
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
        // 예상 응답 메시지: "잘못된 액티비티 요청입니다: title: must not be empty"
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("title: Title must not be blank"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-2. 액티비티 생성 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 생성 실패 테스트: 인증 실패 (401 Unauthorized)")
    void createActivity_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
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

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(post("/api/v1/activity/create")
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
     * 2. 특정 ID로 액티비티 세부 정보 조회 성공 테스트
     */
    @Test
    @DisplayName("특정 ID로 액티비티 세부 정보 조회 성공 테스트 (인증 사용)")
    void findOneActivityDetailById_ShouldReturn200() throws Exception {
        // Given
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
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseStatus.ACTIVITY_SEARCHED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-1. 액티비티 세부 정보 조회 실패 테스트: 존재하지 않는 ID (404 Not Found)
     */
    @Test
    @DisplayName("액티비티 세부 정보 조회 실패 테스트: 존재하지 않는 ID (404 Not Found)")
    void findOneActivityDetailById_NotFound_ShouldReturn404() throws Exception {
        // Given: 존재하지 않는 액티비티 ID
        Long nonExistentId = 999L;

        Mockito.when(activityService.findOneActivityDetailById(nonExistentId))
                .thenThrow(new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND));

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/detail/" + nonExistentId)
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-2. 액티비티 세부 정보 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 세부 정보 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void findOneActivityDetailById_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given: 존재하지 않는 액티비티 ID
        Long activityId = 1L;

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/detail/" + activityId)
                .header("Authorization", "Bearer invalid_token"));

        // Then
        // 예상 응답 메시지: "잘못된 쿠키로 접근했습니다!"
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3. 액티비티 업데이트 성공 테스트
     */
    @Test
    @DisplayName("빌더로 생성한 ActivityUpdateRequestDto로 액티비티 업데이트 성공 테스트 (인증 사용)")
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

    /**
     * 3-1. 액티비티 업데이트 실패 테스트: 존재하지 않는 ID (404 Not Found)
     */
    @Test
    @DisplayName("액티비티 업데이트 실패 테스트: 존재하지 않는 ID (404 Not Found)")
    void updateActivity_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given: 존재하지 않는 액티비티 ID
        ActivityUpdateRequestDto updateRequestDto = ActivityUpdateRequestDto.builder()
                .activityId(999L)
                .title("Updated Activity Title")
                .status(ActivityStatus.DONE)
                .maxNumber(15)
                .description("Updated description")
                .place("Busan")
                .placeUrl("http://updated-example.com")
                .expiredAt(LocalDateTime.now().plusDays(2))
                .build();

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        // Mocking the service method to throw exception
        Mockito.doThrow(new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND))
                .when(activityService).updateActivity(Mockito.any(ActivityUpdateRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/activity/update")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "액티비티를 찾을 수 없습니다"
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-2. 액티비티 업데이트 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("액티비티 업데이트 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void updateActivity_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: 잘못된 title (빈 문자열)
        ActivityUpdateRequestDto updateRequestDto = ActivityUpdateRequestDto.builder()
                .activityId(1L)
                .title("")  // 유효하지 않은 title
                .status(ActivityStatus.DONE)
                .maxNumber(15)
                .description("Updated description")
                .place("Busan")
                .placeUrl("http://updated-example.com")
                .expiredAt(LocalDateTime.now().plusDays(2))
                .build();

        String requestBody = objectMapper.writeValueAsString(updateRequestDto);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/activity/update")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "유효하지 않은 액티비티 요청입니다: title: must not be empty"
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("title: Title must not be blank"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-3. 액티비티 업데이트 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 업데이트 실패 테스트: 인증 실패 (401 Unauthorized)")
    void updateActivity_WithInvalidToken_ShouldReturn401() throws Exception {
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

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(put("/api/v1/activity/update")
                .header("Authorization", "Bearer invalid_token")  // 유효하지 않은 토큰
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "잘못된 쿠키로 접근했습니다!"
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4. 액티비티 삭제 성공 테스트
     */
    @Test
    @DisplayName("액티비티를 삭제합니다 (인증 사용)")
    void deleteActivity_ShouldReturn200() throws Exception {
        // Given
        ActivityDeleteRequestDto requestDto = ActivityDeleteRequestDto.builder()
                .activityId(1L)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(activityService).deleteActivity(Mockito.any(ActivityDeleteRequestDto.class));

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

    /**
     * 4-1. 액티비티 삭제 실패 테스트: 존재하지 않는 ID (404 Not Found)
     */
    @Test
    @DisplayName("액티비티 삭제 실패 테스트: 존재하지 않는 ID (404 Not Found)")
    void deleteActivity_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given: 존재하지 않는 액티비티 ID
        ActivityDeleteRequestDto requestDto = ActivityDeleteRequestDto.builder()
                .activityId(999L)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method to throw exception
        Mockito.doThrow(new RepositoryException(ExceptionStatus.ACTIVITY_NOT_FOUND))
                .when(activityService).deleteActivity(Mockito.any(ActivityDeleteRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/activity/delete")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "액티비티를 찾을 수 없습니다"
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.ACTIVITY_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-2. 액티비티 삭제 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("액티비티 삭제 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void deleteActivity_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: activityId가 null인 경우
        ActivityDeleteRequestDto requestDto = ActivityDeleteRequestDto.builder()
                .activityId(null)  // 유효하지 않은 activityId
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/activity/delete")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "유효하지 않은 액티비티 요청입니다: activityId: must not be null"
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value("activityId: ActivityId must not be blank"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-3. 액티비티 삭제 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 삭제 실패 테스트: 인증 실패 (401 Unauthorized)")
    void deleteActivity_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        ActivityDeleteRequestDto requestDto = ActivityDeleteRequestDto.builder()
                .activityId(1L)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/activity/delete")
                .header("Authorization", "Bearer invalid_token")  // 유효하지 않은 토큰
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        // 예상 응답 메시지: "잘못된 쿠키로 접근했습니다!"
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5. 빌더로 생성한 ActivityUpdateRequestDto로 액티비티 업데이트 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    // 해당 테스트는 이미 3-2에서 구현되었습니다.

    /**
     * 5-1. 액티비티 상태별 조회 실패 테스트: 잘못된 상태 값 (400 Bad Request)
     */
    @Test
    @DisplayName("액티비티 상태별 조회 실패 테스트: 잘못된 상태 값 (400 Bad Request)")
    void findActivityByStatus_WithInvalidStatus_ShouldReturn400() throws Exception {
        // Given: 존재하지 않는 상태 값
        String invalidStatus = "INVALID_STATUS";

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/" + invalidStatus)
                .header("Authorization", "Bearer " + token));

        // Then
        // 예상 응답 메시지: "유효하지 않은 액티비티 요청입니다: status: ..." (해당 필드명과 메시지)
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.GENERAL_BAD_REQUEST.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5-2. 액티비티 상태별 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("액티비티 상태별 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void findActivityByStatus_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given: 존재하지 않는 상태 값
        String status = "PROGRESSING";

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/" + status)
                .header("Authorization", "Bearer invalid_token"));

        // Then
        // 예상 응답 메시지: "잘못된 쿠키로 접근했습니다!"
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 6. 모든 액티비티 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("모든 액티비티 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void findAllActivity_WithInvalidToken_ShouldReturn401() throws Exception {
        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(get("/api/v1/activity/all")
                .header("Authorization", "Bearer invalid_token"));

        // Then
        // 예상 응답 메시지: "잘못된 쿠키로 접근했습니다!"
        MvcResult mvcResult = resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }
}
