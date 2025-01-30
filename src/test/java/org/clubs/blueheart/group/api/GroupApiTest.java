package org.clubs.blueheart.group.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.group.application.GroupService;
import org.clubs.blueheart.group.dto.request.GroupInfoRequestDto;
import org.clubs.blueheart.group.dto.request.GroupUserRequestDto;
import org.clubs.blueheart.group.dto.response.GroupUserInfoResponseDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GroupApi 컨트롤러에 대한 통합 테스트 클래스
 */
@SpringBootTest
@AutoConfigureMockMvc
class GroupApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtGenerator jwtGenerator;  // 실제 Bean 주입

    @MockitoBean
    private GroupService groupService;

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

    /**
     * 1. 그룹 생성 테스트
     */
    @Test
    @DisplayName("그룹 생성 성공 테스트 (인증 필요)")
    void createGroup_ShouldReturn201() throws Exception {
        // Given
        GroupInfoRequestDto requestDto = GroupInfoRequestDto.builder()
                .userId(1L)
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(groupService).createGroup(ArgumentMatchers.any(GroupInfoRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/create")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ResponseStatus.GROUP_CREATED.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseStatus.GROUP_CREATED.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 1-1. 그룹 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("그룹 생성 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void createGroup_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: userId가 null인 경우
        GroupInfoRequestDto requestDto = GroupInfoRequestDto.builder()
                .userId(null)  // 유효하지 않은 userId
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("userId: User ID must not be null"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2. 그룹 삭제 테스트
     */
    @Test
    @DisplayName("그룹 삭제 성공 테스트 (인증 필요)")
    void deleteGroup_ShouldReturn200() throws Exception {
        // Given
        GroupInfoRequestDto requestDto = GroupInfoRequestDto.builder()
                .userId(1L)
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(groupService).deleteGroup(ArgumentMatchers.any(GroupInfoRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/delete")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ResponseStatus.GROUP_DELETED.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseStatus.GROUP_DELETED.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 2-1. 그룹 삭제 실패 테스트: 존재하지 않는 그룹 ID (404 Not Found)
     */
    @Test
    @DisplayName("그룹 삭제 실패 테스트: 존재하지 않는 그룹 ID (404 Not Found)")
    void deleteGroup_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given
        GroupInfoRequestDto requestDto = GroupInfoRequestDto.builder()
                .userId(999L)  // 존재하지 않는 그룹 ID
                .username("홍길동")
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        Mockito.doThrow(new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND))
                .when(groupService).deleteGroup(Mockito.any(GroupInfoRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/delete")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GROUP_NOT_FOUND.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionStatus.GROUP_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3. 그룹에 사용자 추가 테스트
     */
    @Test
    @DisplayName("그룹에 사용자 추가 성공 테스트 (인증 필요)")
    void addGroupUser_ShouldReturn200() throws Exception {
        // Given
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(1L)
                .userId(2L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(groupService).addGroupUserById(ArgumentMatchers.any(GroupUserRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/add")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ResponseStatus.GROUP_ADD.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseStatus.GROUP_ADD.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 3-1. 그룹에 사용자 추가 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("그룹에 사용자 추가 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void addGroupUser_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: groupId가 null인 경우
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(null)  // 유효하지 않은 groupId
                .userId(2L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/add")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("groupId: Group ID must not be null"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4. 그룹에서 사용자 제거 테스트
     */
    @Test
    @DisplayName("그룹에서 사용자 제거 성공 테스트 (인증 필요)")
    void removeGroupUser_ShouldReturn200() throws Exception {
        // Given
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(1L)
                .userId(2L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Mocking the service method
        Mockito.doNothing().when(groupService).removeGroupUserById(ArgumentMatchers.any(GroupUserRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/remove")
                .header("Authorization", "Bearer " + token)  // 토큰 첨부
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ResponseStatus.GROUP_REMOVE.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseStatus.GROUP_REMOVE.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()) // 데이터 없음
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }
    /**
     * 4-1. 그룹에서 사용자 제거 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)
     */
    @Test
    @DisplayName("그룹에서 사용자 제거 실패 테스트: 잘못된 입력 데이터 (400 Bad Request)")
    void removeGroupUser_WithInvalidInput_ShouldReturn400() throws Exception {
        // Given: groupId가 null인 경우
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(null)  // 유효하지 않은 groupId
                .userId(2L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/remove")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GENERAL_BAD_REQUEST.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("groupId: Group ID must not be null"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-2. 그룹에서 사용자 제거 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("그룹에서 사용자 제거 실패 테스트: 인증 실패 (401 Unauthorized)")
    void removeGroupUser_WithInvalidToken_ShouldReturn401() throws Exception {
        // Given
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(1L)
                .userId(2L)
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/remove")
                .header("Authorization", "Bearer invalid_token")  // 유효하지 않은 토큰
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 4-3. 그룹에서 사용자 제거 실패 테스트: 존재하지 않는 사용자 (404 Not Found)
     */
    @Test
    @DisplayName("그룹에서 사용자 제거 실패 테스트: 존재하지 않는 사용자 (404 Not Found)")
    void removeGroupUser_WithNonExistentUser_ShouldReturn404() throws Exception {
        // Given: 존재하지 않는 userId
        GroupUserRequestDto requestDto = GroupUserRequestDto.builder()
                .groupId(1L)
                .userId(999L)  // 존재하지 않는 userId
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        Mockito.doThrow(new RepositoryException(ExceptionStatus.GROUP_USER_NOT_FOUND))
                .when(groupService).removeGroupUserById(Mockito.any(GroupUserRequestDto.class));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/remove")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GROUP_USER_NOT_FOUND.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionStatus.GROUP_USER_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }


    /**
     * 5. 현재 사용자의 그룹 정보 조회 테스트
     */
    @Test
    @DisplayName("현재 사용자의 그룹 정보 조회 성공 테스트 (인증 필요)")
    void getMyGroupInfo_ShouldReturn200() throws Exception {
        // Given
        List<GroupUserInfoResponseDto> mockGroupInfo = List.of(
                GroupUserInfoResponseDto.builder()
                        .userId(1L)
                        .studentNumber("201234567")
                        .username("User1")
                        .role(UserRole.USER)
                        .build(),
                GroupUserInfoResponseDto.builder()
                        .userId(2L)
                        .studentNumber("201234568")
                        .username("User2")
                        .role(UserRole.USER)
                        .build()
        );

        Mockito.when(groupService.getMyGroupInfoByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(mockGroupInfo);

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group/me")
                .header("Authorization", "Bearer " + token));  // 토큰 첨부

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ResponseStatus.GROUP_SEARCHED.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseStatus.GROUP_SEARCHED.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(mockGroupInfo.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].studentNumber").value("201234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("User1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].role").value("USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].studentNumber").value("201234568"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].username").value("User2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].role").value("USER"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5-1. 현재 사용자의 그룹 정보 조회 실패 테스트: 인증 실패 (401 Unauthorized)
     */
    @Test
    @DisplayName("현재 사용자의 그룹 정보 조회 실패 테스트: 인증 실패 (401 Unauthorized)")
    void getMyGroupInfo_WithInvalidToken_ShouldReturn401() throws Exception {
        // When: 유효하지 않은 토큰 사용
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group/me")
                .header("Authorization", "Bearer invalid_token"));  // 유효하지 않은 토큰

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

    /**
     * 5-2. 현재 사용자의 그룹 정보 조회 실패 테스트: 그룹 정보 없음 (404 Not Found)
     */
    @Test
    @DisplayName("현재 사용자의 그룹 정보 조회 실패 테스트: 그룹 정보 없음 (404 Not Found)")
    void getMyGroupInfo_WithNoGroup_ShouldReturn404() throws Exception {
        // Mocking the service to throw exception
        Mockito.when(groupService.getMyGroupInfoByUserId(ArgumentMatchers.anyLong()))
                .thenThrow(new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group/me")
                .header("Authorization", "Bearer " + token));

        // Then
        MvcResult mvcResult = resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ExceptionStatus.GROUP_NOT_FOUND.getStatusCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionStatus.GROUP_NOT_FOUND.getMessage()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("Response: " + mvcResult.getResponse().getContentAsString());
    }

}
