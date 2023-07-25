package com.poolaeem.poolaeem.user.domain.service.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.event.FileEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.file.application.FileDelete;
import com.poolaeem.poolaeem.user.application.SignService;
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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    private final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCapimEo0r638zB\n" +
            "/uWlbjm1FZctyaoiNegUOEFXyvf0lyXI1hSK/Cn2oaL+M9z5z9g/jogfc4ukygfm\n" +
            "Mq0OE2A49acEQsfQLcMFjwerncPk4HfaNtkfeEfqYXYgrKDaVf8/fjtLQ0oFmPIm\n" +
            "BDuHl/7vEy4IoxFy/BCVrYFZzW+iqViNyH2XSbaKxgMOYkqNf6y+dcSWfD/TJWtr\n" +
            "OlmpmWGG1Zw3EWlixThAzWoyPYHdGOLqTjQ5pUgDpMOEAQOWVofKNFCXBzP4cN7f\n" +
            "qGm8DAq45acSKY7LcMVBPMo89aoB7c//+jhBdKzprJQc9KGS2Do98U+ZzpOiSuZG\n" +
            "8Cgr9bdtAgMBAAECggEAOm8MgwScYTzNBoREy4Rho627yN5JPdH2liVHc1/K3w15\n" +
            "hnB1pKpxRLKAfuKj3XSOdj1p69Bh9GsuHuoI/6eMlRkk+XQuQdf4t5H6yPq8uS+j\n" +
            "ub09vylYqxQWaXQw9y7FWn1c9CxBvOm0nW69+5cQCc/KdhL/ld3uek/r7z2zr9cM\n" +
            "qVN2RbRwWoreZflRm6l4Z3WWEcvfkrcupADqcmBd4y5ndQhGC09QjV/xl1Www7OZ\n" +
            "Dnij+mHIrzGYIc4+xbkZstmqCHnoBFTrjLgXiLsxPTDWcQYJTS7jUT6vMqDl850b\n" +
            "62pC4c5hoaO507hGGWGijipTLBU3ijmIoJxn74llgQKBgQDLypYoALIkxKFycnEF\n" +
            "jY6w4Jal/W441WBy2oISl/pPs2wjm7+daCt0WrHc6igk1rfc0DX4iOFwC8hok78n\n" +
            "k61XsOleKGJyNcTYBkdTADtoESY4hnHnhM+jd2aPZNHrwccDNV3O7dJKqxZu/+EP\n" +
            "ps/xfwXo1UYCvNZrghCrA8ujjQKBgQDCRKMyzzoFLWWCQvn9IVJrOngbLrAKatvL\n" +
            "KKwi2g54IAN66FcmrbPuoQjHRl/w0sSXdsHgCzmaZ9bSLul5Ta9i5nemVSvelzYv\n" +
            "YLplbamZcK0rGiD5QQY2Oaxghej/Iw9Od0johlmO73n1cMBPQXqM/9VGTVpZQ1Af\n" +
            "UUs0ixh7YQKBgQCVpc0/0MsAHvgjL5B5MGrwzesyNvjCeUXa8UQE3V8tOacWOt+p\n" +
            "ef428gacH8Kc0qOvXaHUQMey3KQszxnWuSXYOAwWXXd1QLCspWeotonpMHatmWR5\n" +
            "S1iiRCKm97UC94fpfj3Fn3QF6r6Ngg7FzIv0XX98a2h+yCZ4Scltm1rlXQKBgQCr\n" +
            "qCSgf9Wfv4ZDfNUSY8BE+YwpUK7C9bszH8RCo3VHldodX3GqvrAQOoDcPIYH8OU/\n" +
            "hPM4+hIzKm/6lvM8eXVxKX4uHnI+JFT/tjPd7fF/oL2TKWpbdKZ9gaacP5cq4pri\n" +
            "1ao5tIUsNNUKGFAFeDFGp8SXc0BTQZRpp9aRHhtK4QKBgQCDWS7XApelYPb5iuTG\n" +
            "LajzDvL6okGRqh3PZB4l4zRF+7Qfm0YwHvTq5nIthY+XPt1NWvwJlrEWOKv0oqvR\n" +
            "llolMSf8Cid4ErgB3/nPVTJHdtC1RwPsnxUqeuPT6kWyrKknTBjlNUk1q3EiHy++\n" +
            "G9fl+xp0QlhakvqWAwyAnU3n8g==\n" +
            "-----END PRIVATE KEY-----";
    private final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmqYphKNK+t/Mwf7lpW45\n" +
            "tRWXLcmqIjXoFDhBV8r39JclyNYUivwp9qGi/jPc+c/YP46IH3OLpMoH5jKtDhNg\n" +
            "OPWnBELH0C3DBY8Hq53D5OB32jbZH3hH6mF2IKyg2lX/P347S0NKBZjyJgQ7h5f+\n" +
            "7xMuCKMRcvwQla2BWc1voqlYjch9l0m2isYDDmJKjX+svnXElnw/0yVrazpZqZlh\n" +
            "htWcNxFpYsU4QM1qMj2B3Rji6k40OaVIA6TDhAEDllaHyjRQlwcz+HDe36hpvAwK\n" +
            "uOWnEimOy3DFQTzKPPWqAe3P//o4QXSs6ayUHPShktg6PfFPmc6TokrmRvAoK/W3\n" +
            "bQIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    @Spy
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(PRIVATE_KEY, PUBLIC_KEY);

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰을 새로 발급할 수 있다.")
    void testGenerateOnlyAccessTokenByRefreshToken() {
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

        String accessToken = signService.generateAccessTokenByRefreshToken(refreshToken);
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