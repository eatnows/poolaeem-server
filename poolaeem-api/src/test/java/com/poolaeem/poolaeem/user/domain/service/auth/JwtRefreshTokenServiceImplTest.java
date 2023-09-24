package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.JWT;
import com.poolaeem.poolaeem.common.exception.jwt.ExpiredTokenException;
import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInUserJwt;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInUserJwtRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 라프레시 토큰 관리 테스트")
class JwtRefreshTokenServiceImplTest {
    @InjectMocks
    private JwtRefreshTokenServiceImpl jwtRefreshTokenService;
    @Mock
    private LoggedInUserJwtRepository loggedInUserJwtRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("유저의 기존 리프레시 토큰을 삭제하고 새 리프레시 토큰을 관리할 수 있다")
    void testAddRefreshToken() {
        String userId = "user-id";
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";
        MockHttpServletRequest request = new MockHttpServletRequest();

        given(jwtTokenUtil.decode(refreshToken))
                .willReturn(JWT.decode(refreshToken));
        given(loggedInUserJwtRepository.findAllByUserId(userId))
                .willReturn(List.of(new LoggedInUserJwt(userId, refreshToken, "0.0.0.0", "iPhone", ZonedDateTime.now(), ZonedDateTime.now())));

        jwtRefreshTokenService.addRefreshToken(userId, refreshToken, request);

        verify(loggedInUserJwtRepository, times(1)).deleteAll(any());
        verify(loggedInUserJwtRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("로그인한 유저의 리프레시 토큰와 일치하지 않으면 InvalidTokenException 이 발생한다.")
    void testValidRefreshTokenForNotMatched() {
        String refreshToken = "refresh-token";
        MockHttpServletRequest request = new MockHttpServletRequest();

        given(loggedInUserJwtRepository.findByTokenAndClientIpAndUserAgent(anyString(), anyString(), anyString()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> jwtRefreshTokenService.validRefreshToken(refreshToken, request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("로그인한 유저의 리프레시 토큰이 만료된 상태라면 ExpiredTokenException 이 발생한다.")
    void testValidRefreshTokenForExpiresToken() {
        String refreshToken = "refresh-token";
        MockHttpServletRequest request = new MockHttpServletRequest();

        given(loggedInUserJwtRepository.findByTokenAndClientIpAndUserAgent(anyString(), anyString(), anyString()))
                .willReturn(Optional.of(new LoggedInUserJwt("user-id", "refresh-token", "0.0.0.0", "iPhone", ZonedDateTime.now().minusDays(2), ZonedDateTime.now().minusDays(1))));

        assertThatThrownBy(() -> jwtRefreshTokenService.validRefreshToken(refreshToken, request))
                .isInstanceOf(ExpiredTokenException.class);
    }
}