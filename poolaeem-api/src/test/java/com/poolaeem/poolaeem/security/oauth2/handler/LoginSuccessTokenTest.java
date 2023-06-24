package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class LoginSuccessTokenTest {

    private JwtTokenUtil jwtTokenUtil;
    private LoginSuccessToken loginSuccessToken;

    public LoginSuccessTokenTest() {
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        this.loginSuccessToken = new LoginSuccessToken(jwtTokenUtil);
    }

    @Test
    @DisplayName("httpServletResponse 에 accessToken, refreshToken 추가")
    void addAccessTokenAndRefreshToken() {
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        String accessToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6bnVsbCwiZW1haWwiOiJ0ZXN0QHBvb2xhZWVtLmNvbSIsIm5hbWUiOiLtkoDrgrTsnoQiLCJpYXQiOjE2ODc2MTM5MjMsImV4cCI6MTY4NzYxNTcyM30.cCJNYpC8zB-GdqNRyVJ5oySiJspo3stT0pK_wRhvaR0CNYyDj5JE4CExbeFcwxlo09kOpZ5aPO9_wTFzqFtzxwUOBbrNox0pAMr_xe43S2YYWKNo53DD_Jy4KyR010n1a-n4mf_fmhpJCBmKMFk5p9o3yOvlgL2EYqM3oKoiA18nH8dA64OHu2ZznxUZgh6udQjufDppXB6BCgwlTU0HkxyKDlEF9l0nQ2iYoEFx6hUnwllt_0G1gL8BXEzchcnk1hJSzkd67ryR9b3hiCG4Zr0jAakNzypqt-LLMY0FnZ0YdDNWZKfLUAk-2aUsay09E5734CfFImT6JCUbsXNRZQ";
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6bnVsbCwiZW1haWwiOiJ0ZXN0QHBvb2xhZWVtLmNvbSIsIm5hbWUiOiLtkoDrgrTsnoQiLCJpYXQiOjE2ODc2MTM5MjMsImV4cCI6MTY4ODkwOTkyM30.mJ6msq6mNKmND0zfFGNiCBihwN7Njpb7LxDXR7-DMxdhfIl7nMpahtNeRdZDbpt0p-7zkAN_kKBSXbJrDBw7g_WLsTy87nPh78dUooDgzT8lppnstMWbS4ScCmqK9bUifLkzO62i-qwhPiz2rwYfKf2QOHySbhPMRZ7cIgulVIRYDTGdkj3iWShvxAfZbSq8TDCeYcYZl6yQz6LZzILk7XFNDcDx1u5pnGqaKSrLo7sMNwNFjNHr7vqwCBAgYLTxTelg3Z9mCfHVwzMe6b448Iy7cEGRpKC1JxviwC41lxRAFK0SBn23qVodrA-YouzHxJUuvQXcNSFdDIvVM1T6UA";

        assertThat(httpServletResponse.getHeaders("access-token")).isEmpty();
        assertThat(httpServletResponse.getHeaders("refresh-token")).isEmpty();

        given(jwtTokenUtil.generateAccessToken(any()))
                .willReturn(accessToken);
        given(jwtTokenUtil.generateRefreshToken(any()))
                .willReturn(refreshToken);
        loginSuccessToken.addTokenInResponse(httpServletResponse,
                new GenerateTokenUser(
                        "user-id",
                        "test@poolaeem.com",
                        "풀내임",
                        null
                ));

        assertThat(httpServletResponse.getHeaders("access-token").get(0)).isEqualTo(accessToken);
        assertThat(httpServletResponse.getHeaders("refresh-token").get(0)).isEqualTo(refreshToken);
    }
}