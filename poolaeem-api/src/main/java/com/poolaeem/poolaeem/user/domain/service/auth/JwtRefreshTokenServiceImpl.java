package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.common.exception.jwt.ExpiredTokenException;
import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.user.application.JwtRefreshTokenService;
import com.poolaeem.poolaeem.user.domain.dto.RequestClient;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInUserJwt;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInUserJwtRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtRefreshTokenServiceImpl implements JwtRefreshTokenService {
    private final LoggedInUserJwtRepository loggedInUserJwtRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRefreshTokenServiceImpl(LoggedInUserJwtRepository loggedInUserJwtRepository, JwtTokenUtil jwtTokenUtil) {
        this.loggedInUserJwtRepository = loggedInUserJwtRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    @Override
    public void addRefreshToken(String userId, String refreshToken, HttpServletRequest request) {
        removeLoginSession(userId);

        DecodedJWT decodedJWT = jwtTokenUtil.decode(refreshToken);
        Date issuedAt = decodedJWT.getIssuedAt();
        Date expiresAt = decodedJWT.getExpiresAt();

        RequestClient requestClient = new RequestClient(request);
        loggedInUserJwtRepository.save(new LoggedInUserJwt(userId,
                refreshToken,
                requestClient.clientIp(),
                requestClient.userAgent(),
                convertToZonedDateTime(issuedAt),
                convertToZonedDateTime(expiresAt)));
    }

    /**
     * 유저의 refreshToken 과 로그인 당시 clientIp, userAgent 가 맞는지 비교
     */
    @Override
    public DecodedJWT validRefreshToken(String refreshToken, HttpServletRequest request) {
        DecodedJWT decodedJWT = jwtTokenUtil.validRefreshToken(refreshToken);

        LoggedInUserJwt loggedInUser = getAlreadyLoggedInRefreshToken(refreshToken, request);
        checkExpiresToken(loggedInUser);

        return decodedJWT;
    }

    @Transactional
    @Override
    public void removeRefreshToken(String userId) {
        loggedInUserJwtRepository.deleteByUserId(userId);
    }

    @Scheduled(cron = "13 3 3 * * *", zone = TimeComponent.DEFAULT_TIMEZONE)
    @SchedulerLock(name = "removeExpiredRefreshToken", lockAtLeastFor = "PT30S", lockAtMostFor = "PT2M")
    @Transactional
    public void removeExpiredRefreshToken() {
        loggedInUserJwtRepository.deleteAllByExpiresAtBefore(TimeComponent.nowUtc());
        log.info("> 만료된 리프레시 토큰들 제거");
    }

    private static void checkExpiresToken(LoggedInUserJwt loggedInUser) {
        if (loggedInUser.getExpiresAt().isBefore(TimeComponent.nowUtc())) {
            throw new ExpiredTokenException();
        }
    }

    private LoggedInUserJwt getAlreadyLoggedInRefreshToken(String refreshToken, HttpServletRequest request) {
        RequestClient requestClient = new RequestClient(request);
        return loggedInUserJwtRepository.findByTokenAndClientIpAndUserAgent(refreshToken, requestClient.clientIp(), requestClient.userAgent())
                .orElseThrow(InvalidTokenException::new);
    }


    private void removeLoginSession(String userId) {
        List<LoggedInUserJwt> jwts = loggedInUserJwtRepository.findAllByUserId(userId);
        if (!jwts.isEmpty()) {
            loggedInUserJwtRepository.deleteAll(jwts);
        }
    }

    private ZonedDateTime convertToZonedDateTime(Date time) {
        return ZonedDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC);
    }
}
