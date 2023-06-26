package com.poolaeem.poolaeem.common.encrypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위: 텍스트 암호화 테스트")
class AESEncryptorTest {

    AESEncryptor aesEncryptor = new AESEncryptor("ST0IxeU6g1Tbzrqw7k380saqOMaxmUEP");

    @Test
    @DisplayName("텍스트를 암호화할 수 있다.")
    void encrypt() {
        String plainText = "Hello, 암호화 테스트";

        String encryptedText = aesEncryptor.encrypt(plainText);

        assertThat(plainText).isNotEqualTo(encryptedText);
    }

    @Test
    @DisplayName("같은 텍스트를 여러번 암호화해도 암호화된 문자열은 전부 다르다.")
    void encrypt2() {
        String plainText = "Hello, 암호화 테스트";

        String encryptedText1 = aesEncryptor.encrypt(plainText);
        String encryptedText2 = aesEncryptor.encrypt(plainText);
        String encryptedText3 = aesEncryptor.encrypt(plainText);

        assertThat(plainText).isNotEqualTo(encryptedText1);
        assertThat(encryptedText1).isNotEqualTo(encryptedText2);
        assertThat(encryptedText2).isNotEqualTo(encryptedText3);
    }

    @Test
    @DisplayName("암호화된 문자열을 복호화할 수 있다.")
    void decrypt() {
        String plainText = "Hello, 복호화 테스트";

        String encryptedText = aesEncryptor.encrypt(plainText);
        String decryptedText = aesEncryptor.decrypt(encryptedText);

        assertThat(plainText).isNotEqualTo(encryptedText)
                .isEqualTo(decryptedText);
    }
}