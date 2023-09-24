package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.JWT;
import com.poolaeem.poolaeem.common.event.FileEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 인증 관련 테스트")
class SignServiceImplTest {
    @InjectMocks
    private SignServiceImpl signService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @Mock
    private SignUpOAuth2UserService signUpOAuth2UserService;
    @Mock
    private FileEventsPublisher fileEventsPublisher;
    @Mock
    private JwtRefreshTokenServiceImpl jwtRefreshTokenService;

    private final String PRIVATE_KEY = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tICAKTUlJRXZ3SUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2t3Z2dTbEFnRUFBb0lCQVFDYXBpbUVvMHI2Mzh6QgovdVdsYmptMUZaY3R5YW9pTmVnVU9FRlh5dmYwbHlYSTFoU0svQ24yb2FMK005ejV6OWcvam9nZmM0dWt5Z2ZtCk1xME9FMkE0OWFjRVFzZlFMY01Gandlcm5jUGs0SGZhTnRrZmVFZnFZWFlncktEYVZmOC9manRMUTBvRm1QSW0KQkR1SGwvN3ZFeTRJb3hGeS9CQ1ZyWUZaelcraXFWaU55SDJYU2JhS3hnTU9Za3FOZjZ5K2RjU1dmRC9USld0cgpPbG1wbVdHRzFadzNFV2xpeFRoQXpXb3lQWUhkR09McVRqUTVwVWdEcE1PRUFRT1dWb2ZLTkZDWEJ6UDRjTjdmCnFHbThEQXE0NWFjU0tZN0xjTVZCUE1vODlhb0I3Yy8vK2poQmRLenBySlFjOUtHUzJEbzk4VStaenBPaVN1WkcKOENncjliZHRBZ01CQUFFQ2dnRUFPbThNZ3dTY1lUek5Cb1JFeTRSaG82Mjd5TjVKUGRIMmxpVkhjMS9LM3cxNQpobkIxcEtweFJMS0FmdUtqM1hTT2RqMXA2OUJoOUdzdUh1b0kvNmVNbFJraytYUXVRZGY0dDVINnlQcTh1UytqCnViMDl2eWxZcXhRV2FYUXc5eTdGV24xYzlDeEJ2T20wblc2OSs1Y1FDYy9LZGhML2xkM3Vlay9yN3oyenI5Y00KcVZOMlJiUndXb3JlWmZsUm02bDRaM1dXRWN2ZmtyY3VwQURxY21CZDR5NW5kUWhHQzA5UWpWL3hsMVd3dzdPWgpEbmlqK21ISXJ6R1lJYzQreGJrWnN0bXFDSG5vQkZUcmpMZ1hpTHN4UFREV2NRWUpUUzdqVVQ2dk1xRGw4NTBiCjYycEM0YzVob2FPNTA3aEdHV0dpamlwVExCVTNpam1Jb0p4bjc0bGxnUUtCZ1FETHlwWW9BTElreEtGeWNuRUYKalk2dzRKYWwvVzQ0MVdCeTJvSVNsL3BQczJ3am03K2RhQ3QwV3JIYzZpZ2sxcmZjMERYNGlPRndDOGhvazc4biAgCms2MVhzT2xlS0dKeU5jVFlCa2RUQUR0b0VTWTRobkhuaE0ramQyYVBaTkhyd2NjRE5WM083ZEpLcXhadS8rRVAKcHMveGZ3WG8xVVlDdk5acmdoQ3JBOHVqalFLQmdRRENSS015enpvRkxXV0NRdm45SVZKck9uZ2JMckFLYXR2TApLS3dpMmc1NElBTjY2RmNtcmJQdW9RakhSbC93MHNTWGRzSGdDem1hWjliU0x1bDVUYTlpNW5lbVZTdmVsell2CllMcGxiYW1aY0swckdpRDVRUVkyT2F4Z2hlai9JdzlPZDBqb2hsbU83M24xY01CUFFYcU0vOVZHVFZwWlExQWYKVVVzMGl4aDdZUUtCZ1FDVnBjMC8wTXNBSHZnakw1QjVNR3J3emVzeU52akNlVVhhOFVRRTNWOHRPYWNXT3QrcCAgCmVmNDI4Z2FjSDhLYzBxT3ZYYUhVUU1leTNLUXN6eG5XdVNYWU9Bd1dYWGQxUUxDc3BXZW90b25wTUhhdG1XUjUKUzFpaVJDS205N1VDOTRmcGZqM0ZuM1FGNnI2TmdnN0Z6SXYwWFg5OGEyaCt5Q1o0U2NsdG0xcmxYUUtCZ1FDcgpxQ1NnZjlXZnY0WkRmTlVTWThCRStZd3BVSzdDOWJzekg4UkNvM1ZIbGRvZFgzR3F2ckFRT29EY1BJWUg4T1UvCmhQTTQraEl6S20vNmx2TThlWFZ4S1g0dUhuSStKRlQvdGpQZDdmRi9vTDJUS1dwYmRLWjlnYWFjUDVjcTRwcmkKMWFvNXRJVXNOTlVLR0ZBRmVERkdwOFNYYzBCVFFaUnBwOWFSSGh0SzRRS0JnUUNEV1M3WEFwZWxZUGI1aXVURwpMYWp6RHZMNm9rR1JxaDNQWkI0bDR6UkYrN1FmbTBZd0h2VHE1bkl0aFkrWFB0MU5XdndKbHJFV09LdjBvcXZSCmxsb2xNU2Y4Q2lkNEVyZ0IzL25QVlRKSGR0QzFSd1BzbnhVcWV1UFQ2a1d5cktrblRCamxOVWsxcTNFaUh5KysKRzlmbCt4cDBRbGhha3ZxV0F3eUFuVTNuOGc9PQotLS0tLUVORCBQUklWQVRFIEtFWS0tLS0t";
    private final String PUBLIC_KEY = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFtcVlwaEtOSyt0L013ZjdscFc0NQp0UldYTGNtcUlqWG9GRGhCVjhyMzlKY2x5TllVaXZ3cDlxR2kvalBjK2MvWVA0NklIM09McE1vSDVqS3REaE5nIApPUFduQkVMSDBDM0RCWThIcTUzRDVPQjMyamJaSDNoSDZtRjJJS3lnMmxYL1AzNDdTME5LQlpqeUpnUTdoNWYrCjd4TXVDS01SY3Z3UWxhMkJXYzF2b3FsWWpjaDlsMG0yaXNZRERtSktqWCtzdm5YRWxudy8weVZyYXpwWnFabGgKaHRXY054RnBZc1U0UU0xcU1qMkIzUmppNms0ME9hVklBNlREaEFFRGxsYUh5alJRbHdjeitIRGUzNmhwdkF3Swp1T1duRWltT3kzREZRVHpLUFBXcUFlM1AvL280UVhTczZheVVIUFNoa3RnNlBmRlBtYzZUb2tybVJ2QW9LL1czCmJRSURBUUFCCi0tLS0tRU5EIFBVQkxJQyBLRVktLS0tLQ==";
    @Spy
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(PRIVATE_KEY, PUBLIC_KEY);

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰을 새로 발급할 수 있다.")
    void testGenerateOnlyAccessTokenByRefreshToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

        given(jwtRefreshTokenService.validRefreshToken(refreshToken, request))
                .willReturn(JWT.decode(refreshToken));

        String accessToken = signService.generateAccessTokenByRefreshToken(request, refreshToken);
        String[] split = accessToken.split("\\.");
        assertThat(split).hasSize(3);
    }

    @Test
    @DisplayName("유저를 탈퇴 시킬 수 있다.")
    void testDeleteUser() {
        String tokenUserId = "user-id";
        String pathUserId = "user-id";

        User user = new User(tokenUserId, "poolaeem@poolaeem.com", "poolaeem", UserRole.ROLE_USER, OauthProvider.GOOGLE, "1234", null, null);
        given(userRepository.findByIdAndIsDeletedFalse(tokenUserId))
                .willReturn(Optional.of(user));

        assertThat(user.getIsDeleted()).isNull();
        String beforeEmail = user.getEmail();
        String beforeName = user.getEmail();
        String beforeOauthId = user.getOauthId();

        signService.deleteUser(tokenUserId, pathUserId);

        assertThat(user.getIsDeleted()).isTrue();
        assertThat(user.getEmail()).isNotEqualTo(beforeEmail);
        assertThat(user.getName()).isNotEqualTo(beforeName);
        assertThat(user.getOauthId()).isNotEqualTo(beforeOauthId);

        verify(fileEventsPublisher, times(0)).publish(any(EventsPublisherFileEvent.FileDeleteEvent.class));
    }

    @Test
    @DisplayName("유저 탈퇴 시 프로필 이미지가 존재한다면 삭제 이벤트를 발행한다.")
    void testDeleteProfileImageForDeleteUser() {
        String tokenUserId = "user-id";
        String pathUserId = "user-id";

        User user = new User(tokenUserId, "poolaeem@poolaeem.com", "poolaeem", UserRole.ROLE_USER, OauthProvider.GOOGLE, "1234", "asdbc", null);
        given(userRepository.findByIdAndIsDeletedFalse(tokenUserId))
                .willReturn(Optional.of(user));

        assertThat(user.getIsDeleted()).isNull();

        signService.deleteUser(tokenUserId, pathUserId);

        assertThat(user.getIsDeleted()).isTrue();

        verify(fileEventsPublisher, times(1)).publish(any(EventsPublisherFileEvent.FileDeleteEvent.class));
    }

    @Test
    @DisplayName("패스로 요청온 유저id와 유저 토큰의 id가 일치하지 않으면 탈퇴할 수 없다.")
    void testDeleteUserForNotEqualUserId() {
        String tokenUserId = "user-id";
        String pathUserId = "other-user-id";

        assertThatThrownBy(() -> signService.deleteUser(tokenUserId, pathUserId))
                .isInstanceOf(ForbiddenRequestException.class);
    }
}