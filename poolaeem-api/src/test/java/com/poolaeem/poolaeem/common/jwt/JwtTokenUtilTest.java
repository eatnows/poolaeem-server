package com.poolaeem.poolaeem.common.jwt;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

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

//    private final String PRIVATE_KEY = "LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2d0lCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktrd2dnU2xBZ0VBQW9JQkFRQ2FwaW1FbzByNjM4ekIKL3VXbGJqbTFGWmN0eWFvaU5lZ1VPRUZYeXZmMGx5WEkxaFNLL0NuMm9hTCtNOXo1ejlnL2pvZ2ZjNHVreWdmbQpNcTBPRTJBNDlhY0VRc2ZRTGNNRmp3ZXJuY1BrNEhmYU50a2ZlRWZxWVhZZ3JLRGFWZjgvZmp0TFEwb0ZtUEltCkJEdUhsLzd2RXk0SW94RnkvQkNWcllGWnpXK2lxVmlOeUgyWFNiYUt4Z01PWWtxTmY2eStkY1NXZkQvVEpXdHIKT2xtcG1XR0cxWnczRVdsaXhUaEF6V295UFlIZEdPTHFUalE1cFVnRHBNT0VBUU9XVm9mS05GQ1hCelA0Y043ZgpxR204REFxNDVhY1NLWTdMY01WQlBNbzg5YW9CN2MvLytqaEJkS3pwckpRYzlLR1MyRG85OFUrWnpwT2lTdVpHCjhDZ3I5YmR0QWdNQkFBRUNnZ0VBT204TWd3U2NZVHpOQm9SRXk0UmhvNjI3eU41SlBkSDJsaVZIYzEvSzN3MTUKaG5CMXBLcHhSTEtBZnVLajNYU09kajFwNjlCaDlHc3VIdW9JLzZlTWxSa2srWFF1UWRmNHQ1SDZ5UHE4dVMragp1YjA5dnlsWXF4UVdhWFF3OXk3RlduMWM5Q3hCdk9tMG5XNjkrNWNRQ2MvS2RoTC9sZDN1ZWsvcjd6MnpyOWNNCnFWTjJSYlJ3V29yZVpmbFJtNmw0WjNXV0VjdmZrcmN1cEFEcWNtQmQ0eTVuZFFoR0MwOVFqVi94bDFXd3c3T1oKRG5paittSElyekdZSWM0K3hia1pzdG1xQ0hub0JGVHJqTGdYaUxzeFBURFdjUVlKVFM3alVUNnZNcURsODUwYgo2MnBDNGM1aG9hTzUwN2hHR1dHaWppcFRMQlUzaWptSW9KeG43NGxsZ1FLQmdRREx5cFlvQUxJa3hLRnljbkVGCmpZNnc0SmFsL1c0NDFXQnkyb0lTbC9wUHMyd2ptNytkYUN0MFdySGM2aWdrMXJmYzBEWDRpT0Z3Qzhob2s3OG4KazYxWHNPbGVLR0p5TmNUWUJrZFRBRHRvRVNZNGhuSG5oTStqZDJhUFpOSHJ3Y2NETlYzTzdkSktxeFp1LytFUApwcy94ZndYbzFVWUN2TlpyZ2hDckE4dWpqUUtCZ1FEQ1JLTXl6em9GTFdXQ1F2bjlJVkpyT25nYkxyQUthdHZMCktLd2kyZzU0SUFONjZGY21yYlB1b1FqSFJsL3cwc1NYZHNIZ0N6bWFaOWJTTHVsNVRhOWk1bmVtVlN2ZWx6WXYKWUxwbGJhbVpjSzByR2lENVFRWTJPYXhnaGVqL0l3OU9kMGpvaGxtTzczbjFjTUJQUVhxTS85VkdUVnBaUTFBZgpVVXMwaXhoN1lRS0JnUUNWcGMwLzBNc0FIdmdqTDVCNU1Hcnd6ZXN5TnZqQ2VVWGE4VVFFM1Y4dE9hY1dPdCtwCmVmNDI4Z2FjSDhLYzBxT3ZYYUhVUU1leTNLUXN6eG5XdVNYWU9Bd1dYWGQxUUxDc3BXZW90b25wTUhhdG1XUjUKUzFpaVJDS205N1VDOTRmcGZqM0ZuM1FGNnI2TmdnN0Z6SXYwWFg5OGEyaCt5Q1o0U2NsdG0xcmxYUUtCZ1FDcgpxQ1NnZjlXZnY0WkRmTlVTWThCRStZd3BVSzdDOWJzekg4UkNvM1ZIbGRvZFgzR3F2ckFRT29EY1BJWUg4T1UvCmhQTTQraEl6S20vNmx2TThlWFZ4S1g0dUhuSStKRlQvdGpQZDdmRi9vTDJUS1dwYmRLWjlnYWFjUDVjcTRwcmkKMWFvNXRJVXNOTlVLR0ZBRmVERkdwOFNYYzBCVFFaUnBwOWFSSGh0SzRRS0JnUUNEV1M3WEFwZWxZUGI1aXVURwpMYWp6RHZMNm9rR1JxaDNQWkI0bDR6UkYrN1FmbTBZd0h2VHE1bkl0aFkrWFB0MU5XdndKbHJFV09LdjBvcXZSCmxsb2xNU2Y4Q2lkNEVyZ0IzL25QVlRKSGR0QzFSd1BzbnhVcWV1UFQ2a1d5cktrblRCamxOVWsxcTNFaUh5KysKRzlmbCt4cDBRbGhha3ZxV0F3eUFuVTNuOGc9PQotLS0tLUVORCBQUklWQVRFIEtFWS0tLS0t";
//    private final String PUBLIC_KEY = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFtcVlwaEtOSyt0L013ZjdscFc0NQp0UldYTGNtcUlqWG9GRGhCVjhyMzlKY2x5TllVaXZ3cDlxR2kvalBjK2MvWVA0NklIM09McE1vSDVqS3REaE5nCk9QV25CRUxIMEMzREJZOEhxNTNENU9CMzJqYlpIM2hINm1GMklLeWcybFgvUDM0N1MwTktCWmp5SmdRN2g1ZisKN3hNdUNLTVJjdndRbGEyQldjMXZvcWxZamNoOWwwbTJpc1lERG1KS2pYK3N2blhFbG53LzB5VnJhenBacVpsaApodFdjTnhGcFlzVTRRTTFxTWoyQjNSamk2azQwT2FWSUE2VERoQUVEbGxhSHlqUlFsd2N6K0hEZTM2aHB2QXdLCnVPV25FaW1PeTNERlFUektQUFdxQWUzUC8vbzRRWFNzNmF5VUhQU2hrdGc2UGZGUG1jNlRva3JtUnZBb0svVzMKYlFJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t";
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(PRIVATE_KEY, PUBLIC_KEY);

    @Test
    @DisplayName("액세스 토큰을 생성할 수 있다.")
    void generateAccessToken() {
        GenerateTokenUser generateTokenUser = new GenerateTokenUser("id", "email", "name", "image");
        String token = jwtTokenUtil.generateAccessToken(generateTokenUser);

        String[] split = token.split("\\.");
        assertThat(split).hasSize(3);
    }

    @Test
    @DisplayName("리프레쉬 토큰을 생성할 수 있다.")
    void generateRefreshToken() {
        GenerateTokenUser generateTokenUser = new GenerateTokenUser("id", "email", "name", "image");
        String token = jwtTokenUtil.generateRefreshToken(generateTokenUser);

        String[] split = token.split("\\.");
        assertThat(split).hasSize(3);
    }
}