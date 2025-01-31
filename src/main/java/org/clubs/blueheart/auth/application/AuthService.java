package org.clubs.blueheart.auth.application;

import io.jsonwebtoken.Claims;
import org.clubs.blueheart.auth.dao.AuthRepository;
import org.clubs.blueheart.auth.dto.request.AuthInviteAllRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthInviteOneRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthLoginRequestDto;
import org.clubs.blueheart.auth.dto.request.AuthVerifyRequestDto;
import org.clubs.blueheart.auth.dto.response.AuthJwtResponseDto;
import org.clubs.blueheart.config.jwt.JwtGenerator;
import org.clubs.blueheart.exception.ApplicationException;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.user.domain.UserRole;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
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

    public AuthJwtResponseDto loginUserByStudentNumberAndUsername(AuthLoginRequestDto authLoginRequestDto) {
        return authRepository.findUserByStudentNumberAndUsername(authLoginRequestDto);
    }

    public void logoutUser(AuthLoginRequestDto authLoginRequestDto) {
    }

    public void registerUser(UserInfoRequestDto userInfoRequestDto) {
    }

    /**
     * (a) /invite/all 용
     * 초대 JWT에는 creatorUserId, createdAt, expiredAt 담기
     */
    public String inviteAllUser(AuthInviteAllRequestDto authInviteAllRequestDto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("creatorId", authInviteAllRequestDto.getCreatorId());
        payload.put("createdAt", System.currentTimeMillis());

        // 예: 초대 링크 12시간 유효
        long inviteExpireMillis = 12L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, inviteExpireMillis);
    }

    /**
     * (b) /invite 용
     * 초대 JWT에는 creatorUserId, targetUserName, targetStudentNumber, createdAt, expiredAt 담기
     */
    public String inviteUserByStudentNumber(AuthInviteOneRequestDto authInviteOneDto) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("creatorUserId", authInviteOneDto.getCreatorId());
        payload.put("targetUserName", authInviteOneDto.getTargetUsername());
        payload.put("targetStudentNumber", authInviteOneDto.getTargetStudentNumber());
        payload.put("createdAt", System.currentTimeMillis());

        // 예: 초대 링크 12시간 유효
        long inviteExpireMillis = 12L * 60 * 60 * 1000;

        return jwtGenerator.createToken(payload, inviteExpireMillis);
    }

    public String verifyInviteCode(AuthVerifyRequestDto authVerifyRequestDto) {
        Claims claims = jwtGenerator.parseToken(authVerifyRequestDto.getCode());
        // 유효하면 세션 생성
        String sessionId = UUID.randomUUID().toString();
        long sessionExpireTime = System.currentTimeMillis() + SESSION_EXPIRE_MILLIS;
        sessionStore.put(sessionId, sessionExpireTime);

        if (sessionId == null) {
            throw new ApplicationException(ExceptionStatus.GENERAL_INTERNAL_SERVER_ERROR);
        }

        return sessionId;
    }


    public Boolean isSessionValid(String sessionId) {
        if (sessionId == null) throw new ApplicationException(ExceptionStatus.AUTH_SESSION_UNAUTHORIZED);
        Long expireTime = sessionStore.get(sessionId);
        if (expireTime == null) throw new ApplicationException(ExceptionStatus.AUTH_SESSION_UNAUTHORIZED);
        // 만료됐으면 제거
        if (System.currentTimeMillis() > expireTime) {
            sessionStore.remove(sessionId);
            throw new ApplicationException(ExceptionStatus.AUTH_SESSION_UNAUTHORIZED);
        }
        return true;
    }

    /**
     * 최종 로그인 후 발급하는 JWT (id, role, studentNumber, username 등)
     * 여기서는 간단히 HS256 JWT 하나를 만들어 반환
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



}

