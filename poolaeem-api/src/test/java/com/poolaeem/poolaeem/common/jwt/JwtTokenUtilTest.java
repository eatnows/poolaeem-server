package com.poolaeem.poolaeem.common.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenUtilTest {

    private final String PRIVATE_KEY = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2d0lCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktrd2dnU2xBZ0VBQW9JQkFRQ2FwaW1FbzByNjM4ekIKL3VXbGJqbTFGWmN0eWFvaU5lZ1VPRUZYeXZmMGx5WEkxaFNLL0NuMm9hTCtNOXo1ejlnL2pvZ2ZjNHVreWdmbQpNcTBPRTJBNDlhY0VRc2ZRTGNNRmp3ZXJuY1BrNEhmYU50a2ZlRWZxWVhZZ3JLRGFWZjgvZmp0TFEwb0ZtUEltCkJEdUhsLzd2RXk0SW94RnkvQkNWcllGWnpXK2lxVmlOeUgyWFNiYUt4Z01PWWtxTmY2eStkY1NXZkQvVEpXdHIKT2xtcG1XR0cxWnczRVdsaXhUaEF6V295UFlIZEdPTHFUalE1cFVnRHBNT0VBUU9XVm9mS05GQ1hCelA0Y043ZgpxR204REFxNDVhY1NLWTdMY01WQlBNbzg5YW9CN2MvLytqaEJkS3pwckpRYzlLR1MyRG85OFUrWnpwT2lTdVpHCjhDZ3I5YmR0QWdNQkFBRUNnZ0VBT204TWd3U2NZVHpOQm9SRXk0UmhvNjI3eU41SlBkSDJsaVZIYzEvSzN3MTUKaG5CMXBLcHhSTEtBZnVLajNYU09kajFwNjlCaDlHc3VIdW9JLzZlTWxSa2srWFF1UWRmNHQ1SDZ5UHE4dVMragp1YjA5dnlsWXF4UVdhWFF3OXk3RlduMWM5Q3hCdk9tMG5XNjkrNWNRQ2MvS2RoTC9sZDN1ZWsvcjd6MnpyOWNNCnFWTjJSYlJ3V29yZVpmbFJtNmw0WjNXV0VjdmZrcmN1cEFEcWNtQmQ0eTVuZFFoR0MwOVFqVi94bDFXd3c3T1oKRG5paittSElyekdZSWM0K3hia1pzdG1xQ0hub0JGVHJqTGdYaUxzeFBURFdjUVlKVFM3alVUNnZNcURsODUwYgo2MnBDNGM1aG9hTzUwN2hHR1dHaWppcFRMQlUzaWptSW9KeG43NGxsZ1FLQmdRREx5cFlvQUxJa3hLRnljbkVGCmpZNnc0SmFsL1c0NDFXQnkyb0lTbC9wUHMyd2ptNytkYUN0MFdySGM2aWdrMXJmYzBEWDRpT0Z3Qzhob2s3OG4KazYxWHNPbGVLR0p5TmNUWUJrZFRBRHRvRVNZNGhuSG5oTStqZDJhUFpOSHJ3Y2NETlYzTzdkSktxeFp1LytFUApwcy94ZndYbzFVWUN2TlpyZ2hDckE4dWpqUUtCZ1FEQ1JLTXl6em9GTFdXQ1F2bjlJVkpyT25nYkxyQUthdHZMCktLd2kyZzU0SUFONjZGY21yYlB1b1FqSFJsL3cwc1NYZHNIZ0N6bWFaOWJTTHVsNVRhOWk1bmVtVlN2ZWx6WXYKWUxwbGJhbVpjSzByR2lENVFRWTJPYXhnaGVqL0l3OU9kMGpvaGxtTzczbjFjTUJQUVhxTS85VkdUVnBaUTFBZgpVVXMwaXhoN1lRS0JnUUNWcGMwLzBNc0FIdmdqTDVCNU1Hcnd6ZXN5TnZqQ2VVWGE4VVFFM1Y4dE9hY1dPdCtwCmVmNDI4Z2FjSDhLYzBxT3ZYYUhVUU1leTNLUXN6eG5XdVNYWU9Bd1dYWGQxUUxDc3BXZW90b25wTUhhdG1XUjUgClMxaWlSQ0ttOTdVQzk0ZnBmajNGbjNRRjZyNk5nZzdGekl2MFhYOThhMmgreUNaNFNjbHRtMXJsWFFLQmdRQ3IKcUNTZ2Y5V2Z2NFpEZk5VU1k4QkUrWXdwVUs3Qzlic3pIOFJDbzNWSGxkb2RYM0dxdnJBUU9vRGNQSVlIOE9VLwpoUE00K2hJekttLzZsdk04ZVhWeEtYNHVIbkkrSkZUL3RqUGQ3ZkYvb0wyVEtXcGJkS1o5Z2FhY1A1Y3E0cHJpCjFhbzV0SVVzTk5VS0dGQUZlREZHcDhTWGMwQlRRWlJwcDlhUkhodEs0UUtCZ1FDRFdTN1hBcGVsWVBiNWl1VEcKTGFqekR2TDZva0dScWgzUFpCNGw0elJGKzdRZm0wWXdIdlRxNW5JdGhZK1hQdDFOV3Z3SmxyRVdPS3Ywb3F2UgpsbG9sTVNmOENpZDRFcmdCMy9uUFZUSkhkdEMxUndQc254VXFldVBUNmtXeXJLa25UQmpsTlVrMXEzRWlIeSsrCkc5ZmwreHAwUWxoYWt2cVdBd3lBblUzbjhnPT0KLS0tLS1FTkQgUFJJVkFURSBLRVktLS0tLQ==";
    private final String PUBLIC_KEY = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFtcVlwaEtOSyt0L013ZjdscFc0NQp0UldYTGNtcUlqWG9GRGhCVjhyMzlKY2x5TllVaXZ3cDlxR2kvalBjK2MvWVA0NklIM09McE1vSDVqS3REaE5nIApPUFduQkVMSDBDM0RCWThIcTUzRDVPQjMyamJaSDNoSDZtRjJJS3lnMmxYL1AzNDdTME5LQlpqeUpnUTdoNWYrCjd4TXVDS01SY3Z3UWxhMkJXYzF2b3FsWWpjaDlsMG0yaXNZRERtSktqWCtzdm5YRWxudy8weVZyYXpwWnFabGgKaHRXY054RnBZc1U0UU0xcU1qMkIzUmppNms0ME9hVklBNlREaEFFRGxsYUh5alJRbHdjeitIRGUzNmhwdkF3SyAgCnVPV25FaW1PeTNERlFUektQUFdxQWUzUC8vbzRRWFNzNmF5VUhQU2hrdGc2UGZGUG1jNlRva3JtUnZBb0svVzMKYlFJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t";

    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(PRIVATE_KEY, PUBLIC_KEY);

    @Test
    @DisplayName("액세스 토큰을 생성할 수 있다.")
    void generateAccessToken() {
        GenerateTokenUser generateTokenUser = new GenerateTokenUser("id");
        String token = jwtTokenUtil.generateAccessToken(generateTokenUser);

        String[] split = token.split("\\.");
        assertThat(split).hasSize(3);
    }

    @Test
    @DisplayName("리프레쉬 토큰을 생성할 수 있다.")
    void generateRefreshToken() {
        GenerateTokenUser generateTokenUser = new GenerateTokenUser("id");
        String token = jwtTokenUtil.generateRefreshToken(generateTokenUser);

        String[] split = token.split("\\.");
        assertThat(split).hasSize(3);
    }

    @Test
    @DisplayName("액세스 토큰을 검증할 수 있다.")
    void testValidAccessToken() {
        String accessToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6InVzZXItaWQiLCJlbWFpbCI6InRlc3RAcG9vbGFlZW0uY29tIiwibmFtZSI6Iu2SgOuCtOyehCIsImlhdCI6MTY4NzYxMzkyMywiZXhwIjozNjg3NjE1NzIzfQ.ZPSiOO__4_yunYkfdOSrzng-bk1dJ45oga-py4-9YkMY7FdEzNWvPALFuvVl8Vl1Ps4U1VwujSMisaQg7zDWTiMj3hWDaAFPIX-sppWoqAF0HZsO99KGzQOLIaHlPveh8MpYYbacfkRyimmOzhsSVT61rj-gT2RVJQQJnS3jZZuQE408EXYxiFXgUFnUM9JyiKkWHN6AsWhug5h7xpk3yHuUUBqlXaStt-Bqsl5vZCtdfNdSeV47KGwh-vsg3g7gFIX6O9ZCC-uTDwagK1e72uoArtqriXoR1MjQ84GZoWadZNksQEiUDOT5Ctx0B_jk1rXeuxpLlpqSHmdxzlZMsg";

        DecodedJWT decodedJWT = jwtTokenUtil.validAccessToken(accessToken);
        assertThat(decodedJWT.getClaim("code").asString()).isEqualTo("user-id");
    }

    @Test
    @DisplayName("다른 토큰으로는 액세스 토큰을 검증할 수 없다.")
    void testValidAccessTokenByOtherToken() {
        String accessToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

        assertThatThrownBy(() -> jwtTokenUtil.validAccessToken(accessToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("리프레시 토큰을 검증할 수 있다.")
    void testValidRefreshToken() {
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

        DecodedJWT decodedJWT = jwtTokenUtil.validRefreshToken(refreshToken);
        assertThat(decodedJWT.getClaim("code").asString()).isEqualTo("user-id");
    }

    @Test
    @DisplayName("다른 토큰으로는 리프레시 토큰을 검증할 수 없다.")
    void testValidRefreshTokenByOtherToken() {
        String refreshToken = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6InVzZXItaWQiLCJlbWFpbCI6InRlc3RAcG9vbGFlZW0uY29tIiwibmFtZSI6Iu2SgOuCtOyehCIsImlhdCI6MTY4NzYxMzkyMywiZXhwIjozNjg3NjE1NzIzfQ.ZPSiOO__4_yunYkfdOSrzng-bk1dJ45oga-py4-9YkMY7FdEzNWvPALFuvVl8Vl1Ps4U1VwujSMisaQg7zDWTiMj3hWDaAFPIX-sppWoqAF0HZsO99KGzQOLIaHlPveh8MpYYbacfkRyimmOzhsSVT61rj-gT2RVJQQJnS3jZZuQE408EXYxiFXgUFnUM9JyiKkWHN6AsWhug5h7xpk3yHuUUBqlXaStt-Bqsl5vZCtdfNdSeV47KGwh-vsg3g7gFIX6O9ZCC-uTDwagK1e72uoArtqriXoR1MjQ84GZoWadZNksQEiUDOT5Ctx0B_jk1rXeuxpLlpqSHmdxzlZMsg";

        assertThatThrownBy(() -> jwtTokenUtil.validRefreshToken(refreshToken))
                .isInstanceOf(InvalidTokenException.class);
    }
}