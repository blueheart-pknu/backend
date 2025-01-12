package org.clubs.blueheart.auth.application;

import io.jsonwebtoken.Claims;
import org.clubs.blueheart.auth.dao.AuthRepository;
import org.clubs.blueheart.auth.dto.AuthDto;
import org.clubs.blueheart.auth.dto.AuthInviteAllDto;
import org.clubs.blueheart.auth.dto.AuthInviteOneDto;
import org.clubs.blueheart.auth.dto.AuthVerifyDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ApplicationException;
import org.clubs.blueheart.exception.CustomExceptionStatus;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


//TODO: 초대 URL 만드는 방법 논의필요 (jwt같은 형태 생각중)
@Service
public class AuthService {

    // 세션 만료시간: 30분
    private static final long SESSION_EXPIRE_MILLIS = 30L * 60 * 1000;
    private final Map<String, Long> sessionStore = new ConcurrentHashMap<>();
    private final AuthRepository authRepository;
    private final JwtGenerator jwtGenerator;

    public AuthService(AuthRepository authRepository, JwtGenerator jwtGenerator) {
        this.authRepository = authRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public void loginUserByStudentNumberAndUsername(AuthDto authDto) {
    }

    public void logoutUser(AuthDto authDto) {
    }

    public void registerUser(UserInfoDto userInfoDto) {
    }

    /**
     * (a) /invite/all 용
     * 초대 JWT에는 creatorUserId, createdAt, expiredAt 담기
     */
    public String inviteAllUser(AuthInviteAllDto authInviteAllDto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("creatorId", authInviteAllDto.getCreatorId());
        payload.put("createdAt", System.currentTimeMillis());

        // 예: 초대 링크 12시간 유효
        long inviteExpireMillis = 12L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, inviteExpireMillis);
    }

    /**
     * (b) /invite 용
     * 초대 JWT에는 creatorUserId, targetUserName, targetStudentNumber, createdAt, expiredAt 담기
     */
    public String inviteUserByStudentNumber(AuthInviteOneDto authInviteOneDto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("creatorUserId", authInviteOneDto.getCreatorId());
        payload.put("targetUserName", authInviteOneDto.getTargetUsername());
        payload.put("targetStudentNumber", authInviteOneDto.getTargetStudentNumber());
        payload.put("createdAt", System.currentTimeMillis());

        // 예: 초대 링크 12시간 유효
        long inviteExpireMillis = 12L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, inviteExpireMillis);
    }

    public String verifyInviteCode(AuthVerifyDto authVerifyDto) {
        Claims claims = jwtGenerator.parseToken(authVerifyDto.getCode());
        // 유효하면 세션 생성
        String sessionId = UUID.randomUUID().toString();
        long sessionExpireTime = System.currentTimeMillis() + SESSION_EXPIRE_MILLIS;
        sessionStore.put(sessionId, sessionExpireTime);

        if (sessionId == null) {
            throw new ApplicationException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
        }

        return sessionId;
    }


    public boolean isSessionValid(String sessionId) {
        if (sessionId == null) return false;
        Long expireTime = sessionStore.get(sessionId);
        if (expireTime == null) return false;
        // 만료됐으면 제거
        if (System.currentTimeMillis() > expireTime) {
            sessionStore.remove(sessionId);
            return false;
        }
        return true;
    }

    /**
     * 최종 로그인 후 발급하는 JWT (id, role, studentNumber, username 등)
     * 여기서는 간단히 HS256 JWT 하나를 만들어 반환
     */
    public String createLoginJwt(Long userId, String studentNumber, String username, String role) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("studentNumber", studentNumber);
        payload.put("username", username);
        payload.put("role", role);

        // 예: 최종 JWT는 1일 유효
        long finalExpire = 24L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, finalExpire);
    }

    // 예시: 로그인 시도 -> 학번 / 이름 검증
    // 여기서는 “맞다고 치고” 바로 userId=123L, role="USER" 등으로 가정
    public boolean checkUserInfo(String studentNumber, String username) {
        // 실무에선 DB 조회나 다른 로직이 필요
        return (studentNumber != null && username != null && !studentNumber.isEmpty() && !username.isEmpty());
    }



}

