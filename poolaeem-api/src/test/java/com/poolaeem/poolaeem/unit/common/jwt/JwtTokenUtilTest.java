package com.poolaeem.poolaeem.unit.common.jwt;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(PRIVATE_KEY, PUBLIC_KEY);

    @Test
    @DisplayName("액세스 토큰을 생성할 수 있다.")
    void generateAccessToken() {
        String token = jwtTokenUtil.generateAccessToken();

        String[] split = token.split("\\.");
        assertThat(split.length).isEqualTo(3);
    }
}