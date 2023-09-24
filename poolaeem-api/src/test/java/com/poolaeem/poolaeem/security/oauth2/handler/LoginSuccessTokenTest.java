package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.user.application.JwtRefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("단위: 인증 성공 토큰 반환 테스트")
class LoginSuccessTokenTest {

    private JwtTokenUtil jwtTokenUtil;
    private LoginSuccessToken loginSuccessToken;
    private JwtRefreshTokenService jwtRefreshTokenService;

    public LoginSuccessTokenTest() {
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        jwtRefreshTokenService = Mockito.mock(JwtRefreshTokenService.class);
        this.loginSuccessToken = new LoginSuccessToken(jwtTokenUtil, jwtRefreshTokenService);
    }

    @Test
    @DisplayName("httpServletResponse 에 accessToken, refreshToken 추가")
    void addAccessTokenAndRefreshToken() {
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        String accessToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6InVzZXItaWQiLCJlbWFpbCI6InRlc3RAcG9vbGFlZW0uY29tIiwibmFtZSI6Iu2SgOuCtOyehCIsImlhdCI6MTY4NzYxMzkyMywiZXhwIjozNjg3NjE1NzIzfQ.ZPSiOO__4_yunYkfdOSrzng-bk1dJ45oga-py4-9YkMY7FdEzNWvPALFuvVl8Vl1Ps4U1VwujSMisaQg7zDWTiMj3hWDaAFPIX-sppWoqAF0HZsO99KGzQOLIaHlPveh8MpYYbacfkRyimmOzhsSVT61rj-gT2RVJQQJnS3jZZuQE408EXYxiFXgUFnUM9JyiKkWHN6AsWhug5h7xpk3yHuUUBqlXaStt-Bqsl5vZCtdfNdSeV47KGwh-vsg3g7gFIX6O9ZCC-uTDwagK1e72uoArtqriXoR1MjQ84GZoWadZNksQEiUDOT5Ctx0B_jk1rXeuxpLlpqSHmdxzlZMsg";
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

        assertThat(httpServletResponse.getHeaders("access-token")).isEmpty();
        assertThat(httpServletResponse.getHeaders("refresh-token")).isEmpty();

        given(jwtTokenUtil.generateAccessToken(any()))
                .willReturn(accessToken);
        given(jwtTokenUtil.generateRefreshToken(any()))
                .willReturn(refreshToken);
        loginSuccessToken.addTokenInResponse(httpServletRequest,
                httpServletResponse,
                new GenerateTokenUser(
                        "user-id"
                ));

        assertThat(httpServletResponse.getHeaders("access-token").get(0)).isEqualTo(accessToken);
        assertThat(httpServletResponse.getHeaders("refresh-token").get(0)).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("response에 AccessToken만 추가")
    void testAddOnlyAccessToken() {
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        String accessToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6bnVsbCwiZW1haWwiOiJ0ZXN0QHBvb2xhZWVtLmNvbSIsIm5hbWUiOiLtkoDrgrTsnoQiLCJpYXQiOjE2ODc2MTM5MjMsImV4cCI6MTY4NzYxNTcyM30.cCJNYpC8zB-GdqNRyVJ5oySiJspo3stT0pK_wRhvaR0CNYyDj5JE4CExbeFcwxlo09kOpZ5aPO9_wTFzqFtzxwUOBbrNox0pAMr_xe43S2YYWKNo53DD_Jy4KyR010n1a-n4mf_fmhpJCBmKMFk5p9o3yOvlgL2EYqM3oKoiA18nH8dA64OHu2ZznxUZgh6udQjufDppXB6BCgwlTU0HkxyKDlEF9l0nQ2iYoEFx6hUnwllt_0G1gL8BXEzchcnk1hJSzkd67ryR9b3hiCG4Zr0jAakNzypqt-LLMY0FnZ0YdDNWZKfLUAk-2aUsay09E5734CfFImT6JCUbsXNRZQ";

        assertThat(httpServletResponse.getHeaders("access-token")).isEmpty();

        loginSuccessToken.addOnlyAccessTokenInResponse(httpServletResponse, accessToken);

        assertThat(httpServletResponse.getHeaders("access-token").get(0)).isEqualTo(accessToken);
    }
}