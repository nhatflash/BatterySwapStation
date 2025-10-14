package com.swd392.BatterySwapStation.infrastructure.security.session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String SESSION_PREFIX = "session:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String USER_SESSIONS_PREFIX = "user_sessions:";

    public void storeSession(String sessionId, UUID userId, String role, Long expiration) {
        String key = SESSION_PREFIX + sessionId;
        SessionData sessionData = SessionData.builder()
                .userId(userId)
                .role(role)
                .expiresAt(System.currentTimeMillis() + expiration)
                .build();
        redisTemplate.opsForValue().set(key, sessionData, expiration, TimeUnit.MILLISECONDS);

        String userSessionKey = USER_SESSIONS_PREFIX + userId;
        redisTemplate.opsForSet().add(userSessionKey, sessionId);
        redisTemplate.expire(key, expiration, TimeUnit.MILLISECONDS);

    }

    public SessionData getSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        return (SessionData) redisTemplate.opsForValue().get(key);
    }

    public void invalidateSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        redisTemplate.delete(key);
    }

    public void storeRefreshToken(String refreshToken, UUID userId, Long expiration) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(UUID userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void invalidateRefreshToken(UUID userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public void invalidAllUserSessions(UUID userId) {
        String userSessionKey = USER_SESSIONS_PREFIX + userId;
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionKey);

        if (sessionIds != null) {
            for (Object sessionId : sessionIds) {
                String sessionKey = SESSION_PREFIX + sessionId;
                redisTemplate.delete(sessionKey);
            }
        }

        redisTemplate.delete(userSessionKey);
        invalidateRefreshToken(userId);
    }
}
