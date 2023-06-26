package com.poolaeem.poolaeem.common.encrypto;

import com.poolaeem.poolaeem.common.exception.encrypto.DecryptException;
import com.poolaeem.poolaeem.common.exception.encrypto.EncryptException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Component
public class AESEncryptor {
    private final String TRANSFORMATION = "AES/GCM/NoPadding";
    private SecretKey secretKeySpec;
    private final int GCM_IV_LENGTH = 16;
    private final int GCM_TAG_LENGTH = 16;

    public AESEncryptor(@Value("${poolaeem.encrypt.secretKey}") String secretKey) {
        this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String plainText) {
        try {
            byte[] iv = getIv();

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return Base64.encodeBase64String(byteBuffer.array());
        } catch (Exception e) {
            throw new EncryptException();
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] decoded = Base64.decodeBase64(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, decoded, 0, GCM_IV_LENGTH);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

            byte[] decryptText = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

            return new String(decryptText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptException();
        }

    }

    private byte[] getIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        (new SecureRandom()).nextBytes(iv);
        return iv;
    }
}
