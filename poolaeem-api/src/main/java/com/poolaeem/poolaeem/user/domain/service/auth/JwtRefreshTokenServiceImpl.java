package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.user.application.JwtRefreshTokenService;
import com.poolaeem.poolaeem.user.domain.dto.RequestClient;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInUserJwt;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInUserJwtRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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
